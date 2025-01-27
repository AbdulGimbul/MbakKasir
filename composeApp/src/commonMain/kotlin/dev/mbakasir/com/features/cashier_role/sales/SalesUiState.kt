package dev.mbakasir.com.features.cashier_role.sales

data class SalesUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel? = null,
    val draftList: List<dev.mbakasir.com.features.cashier_role.sales.data.ProductDraftWithItems> = emptyList(),
)