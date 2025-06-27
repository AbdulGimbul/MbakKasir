package dev.mbakasir.com.features.auth.presentation.profile

sealed class ProfileUiEvent {
    data object OnShowAlertDialog : ProfileUiEvent()
    data object Logout : ProfileUiEvent()
}