package dev.mbakasir.com.features.auth.presentation.login

sealed class LoginUiState {
    data class NotAuthenticated(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    ) : LoginUiState()

    data object Authenticated : LoginUiState()
}