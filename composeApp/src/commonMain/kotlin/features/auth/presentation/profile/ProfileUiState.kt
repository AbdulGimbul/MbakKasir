package features.auth.presentation.profile

import features.auth.domain.UserData

data class ProfileUiState(
    val user: UserData? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLogout: Boolean = false
)