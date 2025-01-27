package dev.mbakasir.com.features.auth.presentation.profile

import dev.mbakasir.com.features.auth.domain.UserData

data class ProfileUiState(
    val user: UserData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLogout: Boolean = false
)