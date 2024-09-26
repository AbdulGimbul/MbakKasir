package features.auth.presentation.login

sealed class LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    data object Login : LoginUiEvent()
}