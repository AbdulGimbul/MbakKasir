package features.cashier_role.history.presentation

sealed class HistoryUiEvent {
    data class GetHistories(val startDate: String, val endDate: String) : HistoryUiEvent()
}