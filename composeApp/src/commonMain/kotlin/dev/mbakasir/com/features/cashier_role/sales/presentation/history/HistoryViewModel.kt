package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val salesRepository: SalesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.GetHistories -> {
                getHistory(event.startDate, event.endDate)
            }
        }
    }

    private fun getHistory(
        startDate: String = "18-08-2024",
        endDate: String = "20-08-2024",
        page: String = "1",
        perPage: String = "20"
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val result = salesRepository.getHistory(startDate, endDate, page, perPage)
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