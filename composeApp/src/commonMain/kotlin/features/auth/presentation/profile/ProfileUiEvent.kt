package features.auth.presentation.profile

sealed class ProfileUiEvent {
    data object Logout : ProfileUiEvent()
}