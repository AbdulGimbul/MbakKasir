package features.cashier_role.history.presentation

import features.cashier_role.history.domain.HistoryApiModel
import network.NetworkError

data class HistoryUiState(
    val history: HistoryApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null
)