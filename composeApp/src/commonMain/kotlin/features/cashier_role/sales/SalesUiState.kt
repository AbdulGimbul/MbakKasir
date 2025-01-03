package features.cashier_role.sales

import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.ProductTransDraft

data class SalesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: CreatePaymentApiModel? = null,
    val draftList: List<ProductTransDraft> = emptyList(),
)