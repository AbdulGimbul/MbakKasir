package features.cashier_role.history.presentation

import features.cashier_role.history.domain.HistoryApiModel

data class HistoryUiState(
    val history: HistoryApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)