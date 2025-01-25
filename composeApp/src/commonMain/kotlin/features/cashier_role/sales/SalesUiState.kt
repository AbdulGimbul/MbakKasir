package features.cashier_role.sales

import features.cashier_role.sales.data.ProductDraftWithItems
import features.cashier_role.sales.domain.CreatePaymentApiModel

data class SalesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: CreatePaymentApiModel? = null,
    val draftList: List<ProductDraftWithItems> = emptyList(),
)