package features.auth.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.auth.data.AuthRepository
import features.auth.domain.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    init {
        _isLoading.value = true
        checkTokenValidity()
    }

    fun login(username: String, password: String) {
        screenModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.login(LoginRequest(username, password))
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
                        _loginSuccess.value = true
                    }
                }.onError {
                    _errorMessage.value = it
                }

                _isLoading.value = false
            }
        }
    }

    private fun checkTokenValidity() {
        screenModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = authRepository.isTokenValid("", "", "1", "1")
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    _loginSuccess.value = true
                }

                _isLoading.value = false
            }
        }
    }
}