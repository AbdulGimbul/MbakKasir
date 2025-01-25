package features.cashier_role.product.presentation

import features.cashier_role.product.data.ProductEntity
import features.cashier_role.sales.domain.CreatePaymentApiModel

data class ProductUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: CreatePaymentApiModel? = null,
    val productList: List<ProductEntity> = emptyList(),
    val totalProduct: Int = 0
)