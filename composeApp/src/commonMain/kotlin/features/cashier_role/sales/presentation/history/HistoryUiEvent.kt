package features.cashier_role.sales.presentation.history

sealed class HistoryUiEvent {
    data class GetHistories(val startDate: String, val endDate: String) : HistoryUiEvent()
}