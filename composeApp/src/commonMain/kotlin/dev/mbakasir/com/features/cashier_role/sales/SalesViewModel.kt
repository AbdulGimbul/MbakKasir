package dev.mbakasir.com.features.cashier_role.sales

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentRequest
import dev.mbakasir.com.features.cashier_role.sales.domain.toDetailPayload
import dev.mbakasir.com.features.cashier_role.sales.domain.toSerializable
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SalesViewModel(
    private val salesRepository: SalesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState: StateFlow<SalesUiState> = _uiState

    private var currentDraftId: String? = null

    init {
        getDrafts()
    }

    fun onEvent(event: SalesUiEvent) {
        when (event) {
            is SalesUiEvent.SendDraftTrans -> {
                currentDraftId = event.invoiceNumber
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
        val data = _uiState.value.draftList.find { it.draft.draftId == invoiceNumber }
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(
                CreatePaymentRequest(
                    kembali = data?.change.toString(),
                    bayar = data?.draft?.amountPaid.toString(),
                    metode = data?.draft?.paymentMethod.toString(),
                    kasir = "3",
                    cus = "1",
                    nominalPpn = "0",
                    tempo = data?.draft?.dueDate.toString(),
                    detil = data?.items?.map { it.toSerializable().toDetailPayload() }
                        ?: emptyList()
                )
            )
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _uiState.value = _uiState.value.copy(paymentResponse = it)
                        currentDraftId?.let { deleteDraftId ->
                            deleteScannedProducts(deleteDraftId)
                            currentDraftId = null
                        }
                    }
                }.onError {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = "Eh kirim data gagal, coba beberapa saat lagi ya!:')")
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteDraft(draftId)
            _uiState.update { currentState ->
                currentState.copy(
                    draftList = currentState.draftList.filterNot { it.draft.draftId == draftId }
                )
            }
        }
    }
}
