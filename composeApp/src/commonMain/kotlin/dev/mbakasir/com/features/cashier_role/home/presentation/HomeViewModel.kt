package dev.mbakasir.com.features.cashier_role.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.product.domain.toProduct
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val homeRepository: dev.mbakasir.com.features.cashier_role.home.data.HomeRepository,
    private val productRepository: dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())

    init {
        fetchProducts()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val isCacheAvailable = homeRepository.checkCache()

                if (!isCacheAvailable.first()) {
                    val result = productRepository.getProducts()
                    withContext(Dispatchers.Main) {
                        result.onSuccess { data ->
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
}

