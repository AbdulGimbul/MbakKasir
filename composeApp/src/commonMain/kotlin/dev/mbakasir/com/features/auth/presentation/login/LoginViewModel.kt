package dev.mbakasir.com.features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plusmobileapps.konnectivity.Konnectivity
import dev.mbakasir.com.features.auth.data.AuthRepository
import dev.mbakasir.com.features.auth.domain.LoginRequest
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import dev.mbakasir.com.storage.SessionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val konnectivity: Konnectivity,
    private val salesRepository: SalesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.NotAuthenticated())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        checkTokenValidity()
        observeConnectivity()
        getVersion()
    }

    fun onEvent(uiEvent: LoginUiEvent) {
        when (uiEvent) {
            is LoginUiEvent.UsernameChanged -> updateState { it.copy(username = uiEvent.username) }
            is LoginUiEvent.PasswordChanged -> updateState { it.copy(password = uiEvent.password) }
            is LoginUiEvent.Login -> {
                login()
            }
        }
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            konnectivity.isConnectedState.collect { isConnected ->
                withContext(Dispatchers.Main) {
                    updateState { it.copy(isConnected = isConnected) }
                }
            }
        }
    }

    private fun updateState(update: (LoginUiState.NotAuthenticated) -> LoginUiState.NotAuthenticated) {
        _uiState.value =
            (_uiState.value as? LoginUiState.NotAuthenticated)?.let(update) ?: _uiState.value
    }

    private fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            val ui = (_uiState.value as? LoginUiState.NotAuthenticated) ?: return@launch
            updateState { it.copy(isLoading = true, errorMessage = null) }

            val result = authRepository.login(LoginRequest(ui.username, ui.password))
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _uiState.value = LoginUiState.Authenticated(role = it.user.role)
                    }
                }.onError { error ->
                    updateState {
                        it.copy(errorMessage = error.message, isLoading = false)
                    }
                }
            }
        }
    }

    private fun checkTokenValidity() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            val result = authRepository.isTokenValid("", "", "1", "1")
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    val role = authRepository.getRole()
                    _uiState.value = LoginUiState.Authenticated(role = role)
                }.onError { error ->
                    if (error is NetworkException.UnauthorizedException) {
                        salesRepository.deleteAllDrafts()
                        updateState {
                            it.copy(errorMessage = error.message)
                        }
                    } else {
                        // Offline or other error: Check if we have a valid session locally
                        val role = authRepository.getRole()
                        if (role.isNotEmpty()) {
                             _uiState.value = LoginUiState.Authenticated(role = role)
                        } else {
                            updateState {
                                it.copy(errorMessage = error.message)
                            }
                        }
                    }
                }

                updateState { it.copy(isLoading = false) }
            }
        }
    }

    private fun getVersion() {
        viewModelScope.launch {
            val result = authRepository.getVersion()
            withContext(Dispatchers.Main) {
                result.onSuccess { data ->
                    updateState { it.copy(version = data.version) }
                }.onError { error ->
                    updateState {
                        it.copy(errorMessage = error.message)
                    }
                }
            }
        }
    }
}