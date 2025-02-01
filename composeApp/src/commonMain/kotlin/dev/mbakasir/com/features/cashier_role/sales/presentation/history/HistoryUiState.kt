package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import dev.mbakasir.com.features.cashier_role.sales.domain.HistoryApiModel

data class HistoryUiState(
    val startDate: String = "",
    val endDate: String = "",
    val history: HistoryApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)