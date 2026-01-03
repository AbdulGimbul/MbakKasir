package dev.mbakasir.com.features.cashier_role.sales.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
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

    private val _uiState =
            MutableStateFlow(HistoryUiState(startDate = getTodayDate(), endDate = getTodayDate()))
    val uiState: StateFlow<HistoryUiState> = _uiState

    private var currentHistoryPage = 1
    private val historyPerPage = 20

    init {
        getHistory(isReset = true)
    }

    fun onEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.GetHistories -> {
                getHistory(isReset = false)
            }
            is HistoryUiEvent.UpdateDate -> {
                _uiState.value =
                        _uiState.value.copy(startDate = event.startDate, endDate = event.endDate)
                getHistory(isReset = true)
            }
        }
    }

    private fun getHistory(isReset: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        if (isReset) {
            currentHistoryPage = 1
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result =
                    salesRepository.getHistory(
                            startDate = _uiState.value.startDate,
                            endDate = _uiState.value.endDate,
                            page = currentHistoryPage.toString(),
                            perPage = historyPerPage.toString()
                    )

            withContext(Dispatchers.Main) {
                result
                        .onSuccess { response ->
                            if (response.code == "200") {
                                val updatedHistory =
                                        if (isReset) {
                                            response
                                        } else {
                                            _uiState.value.history?.let { currentHistory ->
                                                currentHistory.copy(
                                                        data = currentHistory.data + response.data
                                                )
                                            }
                                                    ?: response
                                        }

                                _uiState.value = _uiState.value.copy(history = updatedHistory)
                                currentHistoryPage++
                            }
                        }
                        .onError { error ->
                            _uiState.value = _uiState.value.copy(errorMessage = error.message)
                        }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}
