package dev.mbakasir.com.features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
        private val authRepository: AuthRepository,
        private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        if (_uiState.value.user == null) {
            getUserData()
        }
        getVersion()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.Logout -> logout()
            is ProfileUiEvent.OnShowAlertDialog ->
                    _uiState.update { it.copy(showDialog = !it.showDialog) }
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
                // Perform local cleanup and navigation regardless of result
                // salesRepository.deleteAllDrafts() // Removed to persist drafts per user

                result
                        .onSuccess {
                            _uiState.value = _uiState.value.copy(isLogout = true, isLoading = false)
                        }
                        .onError {
                            // Even if API fails, we still consider the user logged out locally
                            _uiState.value =
                                    _uiState.value.copy(
                                            isLogout = true,
                                            isLoading = false,
                                            errorMessage =
                                                    it.message // Optional: show error briefly? But
                                            // we are navigating away.
                                            )
                        }
            }
        }
    }

    private fun getVersion() {
        viewModelScope.launch {
            viewModelScope.launch {
                val result = authRepository.getVersion()
                withContext(Dispatchers.Main) {
                    result
                            .onSuccess { data ->
                                _uiState.value = _uiState.value.copy(version = data.version)
                            }
                            .onError { error ->
                                _uiState.value = _uiState.value.copy(errorMessage = error.message)
                            }
                }
            }
        }
    }
}
