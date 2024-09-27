package features.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.auth.data.AuthRepository
import features.auth.domain.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess
import storage.SessionHandler

class LoginViewModel(
    private val sessionHandler: SessionHandler,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.NotAuthenticated())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        checkTokenValidity()
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
                        sessionHandler.setUserData(
                            username = it.user.username,
                            nama = it.user.nama,
                            role = it.user.role,
                            namaToko = it.toko.nama,
                            alamat = it.toko.alamat,
                            telp = it.toko.telp,
                            token = it.token
                        )
                        _uiState.value = LoginUiState.Authenticated
                    }
                }.onError { error ->
                    updateState {
                        it.copy(errorMessage = error.message)
                    }

                    updateState { it.copy(isLoading = false) }
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
                    _uiState.value = LoginUiState.Authenticated
                }.onError { error ->
                    updateState {
                        it.copy(errorMessage = error.message)
                    }
                }

                updateState { it.copy(isLoading = false) }
            }
        }
    }
}