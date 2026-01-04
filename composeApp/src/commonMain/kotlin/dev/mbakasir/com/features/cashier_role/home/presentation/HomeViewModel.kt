package dev.mbakasir.com.features.cashier_role.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.cashier_role.home.data.HomeRepository
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProduct
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
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
    private val authRepository: AuthRepository,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val lastUpdateMaster = MutableStateFlow("")

    init {
        if (_uiState.value.user == null) {
            getUserData()
        }
        getSalesReport()
        fetchProducts()
        fetchCustomers()
    }

    fun refresh() {
        getUserData()
        getSalesReport()
        fetchProducts()
        fetchCustomers()
    }

    private fun fetchCustomers() {
        viewModelScope.launch(Dispatchers.IO) { salesRepository.getCustomers() }
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val lastUpdateCache = productRepository.getLastUpdateCache()
                val getLastUpdateMaster = productRepository.getLastUpdateMaster()

                withContext(Dispatchers.Main) {
                    getLastUpdateMaster
                        .onSuccess {
                            lastUpdateMaster.value = it.lastUpdate.toString()

                            if (lastUpdateCache.isEmpty() ||
                                lastUpdateCache == "null" ||
                                lastUpdateCache != lastUpdateMaster.value
                            ) {
                                productRepository.setLastUpdateCache(lastUpdateMaster.value)
                                // Move getProducts here to avoid fetching if not needed or if
                                // master update failed
                                fetchAndCacheProducts()
                            }
                        }
                        .onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                            // Do NOT clear cache on error
                        }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun fetchAndCacheProducts() {
        val getProducts = productRepository.getProducts()
        withContext(Dispatchers.Main) {
            getProducts
                .onSuccess { data ->
                    productRepository.deleteAllProducts() // Only delete if fetch successful
                    data.barangs.forEach { barang ->
                        productRepository.addProduct(barang.toProduct())
                    }
                }
                .onError { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }
        }
    }

    private fun getSalesReport() {
        viewModelScope.launch {
            val result = homeRepository.getSalesReport()
            withContext(Dispatchers.Main) {
                result
                    .onSuccess { data ->
                        _uiState.value =
                            _uiState.value.copy(
                                nominalPenjualan = data.nominalSales.data,
                                jumlahPenjualan = data.totalSales.data,
                                jumlahPembeli = data.totalCustomers.data
                            )
                    }
                    .onError { error ->
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
