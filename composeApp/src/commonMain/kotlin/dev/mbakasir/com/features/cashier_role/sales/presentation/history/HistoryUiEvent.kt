package dev.mbakasir.com.features.cashier_role.sales.presentation.history

sealed class HistoryUiEvent {
    data object GetHistories : HistoryUiEvent()
    data class UpdateDate(val startDate: String, val endDate: String) : HistoryUiEvent()
}