package dev.mbakasir.com.features.cashier_role.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.product.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    private var currentPage = 0
    private val pageSize = 20

    init {
        getTopProduct()
        getTotalProduct()
    }

    fun getTopProduct() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val product = productRepository
                    .getTopProductByStock(pageSize, currentPage * pageSize)
                    .first()

                if (product.isNotEmpty()) {
                    val updatedList = _uiState.value.productList + product
                    val latestCreatedAt = updatedList.maxByOrNull { it.createdAt }?.createdAt
                    _uiState.value = _uiState.value.copy(
                        productList = updatedList,
                        latestUpdate = latestCreatedAt ?: Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault()),
                        isLoading = false
                    )
                    currentPage++
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
                _uiState.value = _uiState.value.copy(
                    totalProduct = product
                )
            }
        }
    }
}