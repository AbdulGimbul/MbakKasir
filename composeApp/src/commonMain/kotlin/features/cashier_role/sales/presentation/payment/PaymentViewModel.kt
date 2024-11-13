package features.cashier_role.sales.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plusmobileapps.konnectivity.Konnectivity
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.toDetailPayload
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess

class PaymentViewModel(private val salesRepository: SalesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState
    private val _connectivity = MutableStateFlow(Konnectivity())

    init {
        val isConnected = _connectivity.value.isConnected
        _uiState.value = _uiState.value.copy(isConnected = isConnected)
    }

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.UangDiterimaChanged -> {
                _uiState.value = _uiState.value.copy(uangDiterima = event.uangDiterima)
                val kembalian =
                    (_uiState.value.uangDiterima.toIntOrNull() ?: 0) - _uiState.value.subtotal
                _uiState.value = _uiState.value.copy(kembalian = kembalian)
            }

            is PaymentUiEvent.DateIconClicked -> {
                _uiState.value = _uiState.value.copy(showDatePicker = true)
            }

            is PaymentUiEvent.DeleteScannedProducts -> {
                deleteScannedProducts(event.draftId)
            }

            is PaymentUiEvent.ConfirmButtonClicked -> {
                createPayment(event.method)
            }

            is PaymentUiEvent.SelectedDateChanged -> {
                _uiState.value =
                    _uiState.value.copy(selectedDate = event.date, showDatePicker = false)
            }

            is PaymentUiEvent.DismissDialog -> {
                _uiState.value = _uiState.value.copy(showDatePicker = false)
            }

            is PaymentUiEvent.ArgumentProductsLoaded -> {
                _uiState.value = _uiState.value.copy(products = event.products)
                val totalHarga = _uiState.value.products.sumOf { it.subtotal }
                val diskon = _uiState.value.products.sumOf { it.diskon }
                val subtotal = totalHarga - diskon
                _uiState.value =
                    _uiState.value.copy(
                        totalHarga = totalHarga,
                        diskon = diskon,
                        subtotal = subtotal
                    )
            }
        }
    }

    private fun createPayment(method: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(
                CreatePaymentRequest(
                    kembali = _uiState.value.kembalian.toString(),
                    bayar = _uiState.value.uangDiterima,
                    metode = method,
                    kasir = "3",
                    cus = "1",
                    nominal_ppn = "0",
                    tempo = _uiState.value.selectedDate,
                    detil = _uiState.value.products.map { it.toDetailPayload() }
                )
            )
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _uiState.value = _uiState.value.copy(paymentResponse = it)
                    }
                }.onError {
                    _uiState.value = _uiState.value.copy(errorMessage = it.message)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteDraft(draftId)
        }
    }
}