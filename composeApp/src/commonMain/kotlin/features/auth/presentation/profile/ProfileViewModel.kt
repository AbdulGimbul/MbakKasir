package features.auth.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.auth.data.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                authRepository.userInfo().let {
                    _uiState.value = _uiState.value.copy(user = it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}