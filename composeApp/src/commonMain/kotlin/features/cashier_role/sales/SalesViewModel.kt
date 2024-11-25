package features.cashier_role.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.toDetailPayload
import features.cashier_role.sales.domain.toSerializable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess

class SalesViewModel(
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState

    init {
        getDrafts()
    }

    fun onEvent(event: SalesUiEvent) {
        when (event) {
            is SalesUiEvent.SendDraftTrans -> {
                sendDraftTrans(event.invoiceNumber)
            }
        }
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

    private fun sendDraftTrans(invoiceNumber: String) {
        val data = _uiState.value.draftList.find { it.draftId == invoiceNumber }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(
                CreatePaymentRequest(
                    kembali = data?.change.toString(),
                    bayar = data?.amountPaid.toString(),
                    metode = data?.paymentMethod.toString(),
                    kasir = "3",
                    cus = "1",
                    nominal_ppn = "0",
                    tempo = data?.dueDate.toString(),
                    detil = data?.items?.map { it.toSerializable().toDetailPayload() }
                        ?: emptyList()
                )
            )
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _uiState.value = _uiState.value.copy(paymentResponse = it)
                    }
                }.onError {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = "Eh kirim data gagal, coba beberapa saat lagi ya!:')")
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}