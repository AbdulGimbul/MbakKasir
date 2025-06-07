package dev.mbakasir.com.features.admin_role.stock_opname.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.admin_role.stock_opname.data.StockOpnameRepository
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import dev.mbakasir.com.utils.getTodayDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StockOpnameViewModel(
    private val stockOpnameRepository: StockOpnameRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        StockOpnameUiState(
            startDate = getTodayDate(),
            endDate = getTodayDate()
        )
    )
    val uiState = _uiState.asStateFlow()

    private var currentStockOpnamePage = 1
    private val stockOpnamePerPage = 20

    init {
        getStockOpname(_uiState.value.startDate, _uiState.value.endDate)
    }

    fun onEvent(event: StockOpnameUiEvent) {
        when (event) {
            is StockOpnameUiEvent.GetStockOpname -> {
                getStockOpname(_uiState.value.startDate, _uiState.value.endDate)
            }

            is StockOpnameUiEvent.UpdateDate -> {
                _uiState.value = _uiState.value.copy(
                    startDate = event.startDate,
                    endDate = event.endDate
                )
            }
        }
    }

    private fun getStockOpname(startDate: String, endDate: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = stockOpnameRepository.getStockOpname(
                startDate = startDate,
                endDate = endDate,
                page = currentStockOpnamePage.toString(),
                perPage = stockOpnamePerPage.toString()
            )

            withContext(Dispatchers.Main) {
                result.onSuccess { response ->
                    if (response.code == "200") {
                        val currentData = _uiState.value.stockOpname?.data ?: emptyList()

                        val newData = if (currentStockOpnamePage == 1) {
                            response.data
                        } else {
                            currentData + response.data
                        }

                        val updatedStockOpname = response.copy(data = newData.distinct())
                        _uiState.value = _uiState.value.copy(stockOpname = updatedStockOpname)
                        currentStockOpnamePage++
                    }
                }.onError { error ->
                    _uiState.value = _uiState.value.copy(errorMessage = error.message)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun loadData() {
        currentStockOpnamePage = 1
        _uiState.value = _uiState.value.copy(
            stockOpname = null,
            errorMessage = null
        )
        getStockOpname(_uiState.value.startDate, _uiState.value.endDate)
    }
}