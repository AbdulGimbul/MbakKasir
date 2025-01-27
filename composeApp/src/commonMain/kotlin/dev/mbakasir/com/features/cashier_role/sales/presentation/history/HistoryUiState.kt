package dev.mbakasir.com.features.cashier_role.sales.presentation.history

data class HistoryUiState(
    val history: dev.mbakasir.com.features.cashier_role.sales.domain.HistoryApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)