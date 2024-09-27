package features.cashier_role.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.history.data.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    init {
        getHistory()
    }

    private fun getHistory(
        startDate: String = "18-08-2024",
        endDate: String = "20-08-2024",
        page: String = "1",
        perPage: String = "5"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = historyRepository.getHistory(startDate, endDate, page, perPage)
            withContext(Dispatchers.Main) {
                result.onSuccess { response ->
                    if (response.code == "200") {
                        _uiState.value = _uiState.value.copy(history = response)
                    }
                }.onError {
                    _uiState.value = _uiState.value.copy(errorMessage = it.message)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}