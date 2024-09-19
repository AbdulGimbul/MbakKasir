package features.auth.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.auth.data.AuthRepository
import features.auth.domain.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ScreenModel {

    private val _user = MutableStateFlow<UserData?>(null)
    val user: StateFlow<UserData?> = _user

    init {
        if (_user.value == null) {
            getUserData()
        }
    }

    private fun getUserData() {
        screenModelScope.launch(Dispatchers.IO) {
            try {
                _user.value = authRepository.userInfo()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}