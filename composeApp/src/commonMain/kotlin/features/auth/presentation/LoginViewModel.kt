package features.auth.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.auth.data.AuthRepository
import features.auth.data.LoginRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import network.NetworkError
import network.onError
import network.onSuccess
import storage.SessionHandler

class LoginViewModel(
    private val sessionHandler: SessionHandler,
    private val authRepository: AuthRepository,
) : ScreenModel {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun login(username: String, password: String) {
        screenModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            authRepository.login(LoginRequest(username, password))
                .onSuccess {
                    if (it.code == "200") {
                        sessionHandler.setUserToken(it.token)
                        _loginSuccess.value = true

                    }
                }
                .onError {
                    _errorMessage.value = it
                }

            _isLoading.value = false
        }
    }
}