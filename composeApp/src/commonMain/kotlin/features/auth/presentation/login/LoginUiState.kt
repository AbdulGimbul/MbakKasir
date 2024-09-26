package features.auth.presentation.login

import network.NetworkError

sealed class LoginUiState {
    data class NotAuthenticated(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: NetworkError? = null
    ) : LoginUiState()

    data object Authenticated : LoginUiState()
}