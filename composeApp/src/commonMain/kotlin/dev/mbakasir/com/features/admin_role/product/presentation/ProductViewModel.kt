package dev.mbakasir.com.features.admin_role.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import dev.mbakasir.com.features.cashier_role.product.domain.toProduct
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    private var currentPage = 0
    private val pageSize = 20

    private val lastUpdateMaster = MutableStateFlow("")

    fun reloadData() {
        currentPage = 0
        fetchProducts()
        getTopProduct()
        getTotalProduct()
    }

    private fun fetchProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val lastUpdateCache = productRepository.getLastUpdateCache()
                _uiState.value = _uiState.value.copy(latestUpdate = lastUpdateCache)
                val getLastUpdateMaster = productRepository.getLastUpdateMaster()

                withContext(Dispatchers.Main) {
                    getLastUpdateMaster
                        .onSuccess {
                            lastUpdateMaster.value = it.lastUpdate.toString()
                            _uiState.value =
                                _uiState.value.copy(latestUpdate = lastUpdateMaster.value)

                            if (lastUpdateCache.isEmpty() ||
                                lastUpdateCache == "null" ||
                                lastUpdateCache != lastUpdateMaster.value
                            ) {
                                productRepository.setLastUpdateCache(lastUpdateMaster.value)

                                val getProducts = productRepository.getProducts()
                                withContext(Dispatchers.Main) {
                                    // productRepository.deleteAllProducts() // Deleted only on
                                    // success
                                    getProducts
                                        .onSuccess { data ->
                                            productRepository.deleteAllProducts()
                                            data.barangs.forEach { barang ->
                                                productRepository.addProduct(
                                                    barang.toProduct()
                                                )
                                            }
                                            getTopProduct()
                                        }
                                        .onError { error ->
                                            _uiState.value =
                                                _uiState.value.copy(
                                                    errorMessage = error.message,
                                                    isLoading = false
                                                )
                                        }
                                }
                            }
                        }
                        .onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                        }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getTopProduct() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val product =
                    productRepository
                        .getTopProductByStock(pageSize, currentPage * pageSize)
                        .first()

                if (product.isNotEmpty()) {
                    val updatedList =
                        if (currentPage == 0) {
                            product
                        } else {
                            _uiState.value.productList + product
                        }
                    _uiState.value =
                        _uiState.value.copy(productList = updatedList, isLoading = false)
                    currentPage++
                } else {
                    if (currentPage == 0) {
                        _uiState.value =
                            _uiState.value.copy(productList = emptyList(), isLoading = false)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(errorMessage = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun getTotalProduct() {
        viewModelScope.launch {
            productRepository.calculateTotalProducts().collectLatest { product ->
                _uiState.value = _uiState.value.copy(totalProduct = product)
            }
        }
    }
}
