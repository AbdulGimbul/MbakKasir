package features.auth.presentation.profile

import features.auth.domain.UserData
import network.NetworkError

data class ProfileUiState(
    val user: UserData? = null,
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null
)