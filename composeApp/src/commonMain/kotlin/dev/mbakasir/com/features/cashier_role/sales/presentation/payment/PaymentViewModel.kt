package dev.mbakasir.com.features.cashier_role.sales.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plusmobileapps.konnectivity.Konnectivity
import dev.mbakasir.com.features.cashier_role.sales.data.SalesRepository
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentRequest
import dev.mbakasir.com.features.cashier_role.sales.domain.toDetailPayload
import dev.mbakasir.com.network.onError
import dev.mbakasir.com.network.onSuccess
import dev.mbakasir.com.utils.getCurrentFormattedDateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel(private val salesRepository: SalesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentUiState())
    val uiState: StateFlow<PaymentUiState> = _uiState
    private val _connectivity = MutableStateFlow(Konnectivity())

    init {
        val isConnected = _connectivity.value.isConnected
        _uiState.value = _uiState.value.copy(isConnected = isConnected)
        getCustomers()
    }

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.UangDiterimaChanged -> {
                val uangDiterimaValue = event.uangDiterima.toIntOrNull() ?: 0
                val kembalian = uangDiterimaValue - _uiState.value.subtotal
                _uiState.value =
                        _uiState.value.copy(
                                uangDiterima = event.uangDiterima,
                                kembalian = kembalian
                        )
            }
            is PaymentUiEvent.DescriptionChanged -> {
                _uiState.value =
                        _uiState.value.copy(
                                description = event.description,
                                uangDiterima = _uiState.value.subtotal.toString()
                        )
            }
            is PaymentUiEvent.DateIconClicked -> {
                _uiState.value = _uiState.value.copy(showDatePicker = true)
            }
            is PaymentUiEvent.DeleteScannedProducts -> {
                deleteScannedProducts(event.draftId)
            }
            is PaymentUiEvent.ConfirmButtonClicked -> {
                createPayment()
            }
            is PaymentUiEvent.PaymentMethodChanged -> {
                val method = event.method.lowercase().replaceFirstChar { it.uppercaseChar() }
                val uangDiterima =
                        if (method.equals("Qris", ignoreCase = true)) {
                            _uiState.value.subtotal.toString()
                        } else {
                            _uiState.value.uangDiterima
                        }

                val uangDiterimaValue = uangDiterima.toIntOrNull() ?: 0
                val kembalian = uangDiterimaValue - _uiState.value.subtotal

                _uiState.value =
                        _uiState.value.copy(
                                paymentMethod = method,
                                uangDiterima = uangDiterima,
                                kembalian = kembalian
                        )
            }
            is PaymentUiEvent.NoInvoiceChanged -> {
                val date = getCurrentFormattedDateTime()
                _uiState.value =
                        _uiState.value.copy(noInvoice = event.noInvoice, currentDate = date)
            }
            is PaymentUiEvent.SelectedDateChanged -> {
                _uiState.value =
                        _uiState.value.copy(selectedDate = event.date, showDatePicker = false)
            }
            is PaymentUiEvent.DismissDialog -> {
                _uiState.value = _uiState.value.copy(showDatePicker = false)
            }
            is PaymentUiEvent.DraftIsPrinted -> {
                updateTransDraftIsPrinted(event.draftId)
            }
            is PaymentUiEvent.ArgumentProductsLoaded -> {
                _uiState.value = _uiState.value.copy(products = event.products)
                val totalHarga = _uiState.value.products.sumOf { it.qtyJual * it.hargaItem }
                val diskon = _uiState.value.products.sumOf { it.diskon }
                val subtotal = totalHarga - diskon
                _uiState.value =
                        _uiState.value.copy(
                                totalHarga = totalHarga,
                                diskon = diskon,
                                subtotal = subtotal
                        )
            }
            is PaymentUiEvent.OnSearchCustChanged -> {
                _uiState.value = _uiState.value.copy(searchCust = event.searchCust)
            }
            is PaymentUiEvent.CustomerReceived -> {
                _uiState.value = _uiState.value.copy(searchCust = event.searchCust)
            }
        }
    }

    private fun createPayment() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result =
                    salesRepository.createPayment(
                            CreatePaymentRequest(
                                    kembali = _uiState.value.kembalian,
                                    bayar = _uiState.value.uangDiterima.toIntOrNull() ?: 0,
                                    metode = _uiState.value.paymentMethod,
                                    keterangan = _uiState.value.description,
                                    kasir = 3,
                                    cus = _uiState.value.searchCust,
                                    ppnPercentage = 0,
                                    nominalPpn = 0,
                                    tempo = _uiState.value.selectedDate,
                                    noInvoice = _uiState.value.noInvoice,
                                    detil = _uiState.value.products.map { it.toDetailPayload() }
                            )
                    )
            withContext(Dispatchers.Main) {
                result
                        .onSuccess {
                            if (it.code == "200") {
                                _uiState.value = _uiState.value.copy(paymentResponse = it)
                            }
                        }
                        .onError { _uiState.value = _uiState.value.copy(errorMessage = it.message) }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun deleteScannedProducts(draftId: String) {
        viewModelScope.launch(Dispatchers.IO) { salesRepository.deleteDraft(draftId) }
    }

    private fun updateTransDraftIsPrinted(draftId: String?) {
        if (draftId != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val uangDiterimaValue = _uiState.value.uangDiterima.toIntOrNull() ?: 0
                salesRepository.updateProductTransInDraft(
                        draftId,
                        amountPaid =
                                if (_uiState.value.uangDiterima.isEmpty()) 0 else uangDiterimaValue,
                        paymentMethod = _uiState.value.paymentMethod,
                        description = _uiState.value.description,
                        customer = _uiState.value.searchCust,
                        isPrinted = true
                )
            }
        }
    }

    private fun getCustomers() {
        viewModelScope.launch {
            val result = salesRepository.getCustomers()
            result
                    .onSuccess { _uiState.value = _uiState.value.copy(customers = it.customers) }
                    .onError {
                        _uiState.value = _uiState.value.copy(customerLoadError = it.message)
                    }
        }
    }
}
