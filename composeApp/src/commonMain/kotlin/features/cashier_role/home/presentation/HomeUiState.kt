package features.cashier_role.home.presentation

import network.NetworkError

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null
)