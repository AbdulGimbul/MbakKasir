package features.cashier_role.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.product.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    init {
        getTopProduct()
        getTotalProduct()
    }

    private fun getTopProduct() {
        viewModelScope.launch {
            productRepository.getTopProductByStock().collectLatest { product ->
                val latestCreatedAt = product.maxByOrNull { it.createdAt }?.createdAt
                _uiState.value = _uiState.value.copy(
                    productList = product,
                    latestUpdate = latestCreatedAt ?: Clock.System.now()
                        .toLocalDateTime(TimeZone.currentSystemDefault())
                )
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