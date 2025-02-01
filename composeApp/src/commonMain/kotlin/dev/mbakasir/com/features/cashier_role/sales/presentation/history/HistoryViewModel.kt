package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import dev.mbakasir.com.utils.getLastWeekDate
import dev.mbakasir.com.utils.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryViewModel(
    private val salesRepository: SalesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HistoryUiState(
            startDate = getLastWeekDate(),
            endDate = getTodayDate()
        )
    )
    val uiState: StateFlow<HistoryUiState> = _uiState

    private var currentHistoryPage = 1
    private val historyPerPage = 20

    init {
        getHistory(_uiState.value.startDate, _uiState.value.endDate)
    }

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.GetHistories -> {
                getHistory(_uiState.value.startDate, _uiState.value.endDate)
            }

            is HistoryUiEvent.UpdateDate -> {
                _uiState.value = _uiState.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate
                )
            }
        }
    }

    private fun getHistory(startDate: String, endDate: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.getHistory(
                startDate = startDate,
                endDate = endDate,
                page = currentHistoryPage.toString(),
                perPage = historyPerPage.toString()
            )

            withContext(Dispatchers.Main) {
                result.onSuccess { response ->
                    if (response.code == "200") {
                        val updatedHistory = _uiState.value.history?.let { currentHistory ->
                            currentHistory.copy(
                                data = currentHistory.data + response.data
                            )
                        } ?: response

                        _uiState.value = _uiState.value.copy(history = updatedHistory)
                        currentHistoryPage++
                    }
                }.onError { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}