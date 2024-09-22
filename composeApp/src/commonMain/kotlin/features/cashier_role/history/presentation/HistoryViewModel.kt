package features.cashier_role.history.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.cashier_role.history.data.HistoryRepository
import features.cashier_role.history.domain.HistoryApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.NetworkError
import network.onError
import network.onSuccess

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ScreenModel {

    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage
    private val _historyResponse = MutableStateFlow<HistoryApiModel?>(null)
    val historyResponse: StateFlow<HistoryApiModel?> = _historyResponse
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        getHistory()
    }

    fun getHistory(
        startDate: String = "18-08-2024",
        endDate: String = "20-08-2024",
        page: String = "1",
        perPage: String = "5"
    ) {
        screenModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _errorMessage.value = null

            val result = historyRepository.getHistory(startDate, endDate, page, perPage)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _historyResponse.value = it
                    }
                }.onError {
                    _errorMessage.value = it
                }

                _isLoading.value = false
            }
        }
    }
}