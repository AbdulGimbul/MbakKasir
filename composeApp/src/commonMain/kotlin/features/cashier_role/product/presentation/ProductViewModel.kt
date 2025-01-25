package features.cashier_role.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.product.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState

    init {
        getTopProduct()
        getTotalProduct()
    }

    private fun getTopProduct(){
        viewModelScope.launch {
            productRepository.getTopProductByStock().collectLatest { product ->
                _uiState.value = _uiState.value.copy(
                    productList = product
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