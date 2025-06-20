package dev.mbakasir.com.features.cashier_role.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.cashier_role.home.data.HomeRepository
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProduct
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val productRepository: ProductRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val lastUpdate = MutableStateFlow("")

    init {
        if (_uiState.value.user == null) {
            getUserData()
        }
        getSalesReport()
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val lastUpdateCache = homeRepository.getLastUpdateCache()
                val getLastUpdateMaster = homeRepository.getLastUpdateMaster()

                withContext(Dispatchers.Main) {
                    getLastUpdateMaster.onSuccess {
                        lastUpdate.value = it.lastUpdate
                    }.onError { error ->
                        _uiState.value = _uiState.value.copy(errorMessage = error.message)
                    }
                }

                if (lastUpdateCache.isEmpty() || lastUpdateCache != lastUpdate.value) {
                    homeRepository.setLastUpdateCache(lastUpdate.value)
                    val getProducts = productRepository.getProducts()
                    withContext(Dispatchers.Main) {
                        getProducts.onSuccess { data ->
                            data.barangs.forEach { barang ->
                                productRepository.addProduct(barang.toProduct())
                            }
                        }.onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun getSalesReport() {
        viewModelScope.launch {
            val result = homeRepository.getSalesReport()
            withContext(Dispatchers.Main) {
                result.onSuccess { data ->
                    _uiState.value = _uiState.value.copy(
                        nominalPenjualan = data.nominalSales.data,
                        jumlahPenjualan = data.totalSales.data,
                        jumlahPembeli = data.totalCustomers.data
                    )
                }.onError { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }
            }

        }
    }

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                authRepository.userInfo().let {
                    _uiState.value = _uiState.value.copy(user = it, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                e.printStackTrace()
            }
        }
    }
}

