package dev.mbakasir.com.features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        if (_uiState.value.user == null) {
            getUserData()
        }
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.Logout -> logout()
        }
    }

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                authRepository.userInfo().let {
                    _uiState.value = _uiState.value.copy(user = it, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                e.printStackTrace()
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = authRepository.logout()
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLogout = true,
                        isLoading = false
                    )
                }.onError {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = it.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}