package features.cashier_role.sales.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.sales.SalesUiState
import features.cashier_role.sales.data.SalesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SalesViewModel(
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState

    init {
        getDrafts()
    }

    private fun getDrafts() {
        viewModelScope.launch {
            salesRepository.getDrafts().collectLatest { drafts ->
                _uiState.value = _uiState.value.copy(
                    draftList = drafts
                )
            }
        }
    }
}