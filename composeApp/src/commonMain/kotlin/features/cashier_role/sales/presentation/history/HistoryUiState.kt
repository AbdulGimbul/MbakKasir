package features.cashier_role.sales.presentation.history

import features.cashier_role.sales.domain.HistoryApiModel

data class HistoryUiState(
    val history: HistoryApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)