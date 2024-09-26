package features.cashier_role.sales.presentation.invoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import features.auth.domain.Toko
import features.cashier_role.sales.data.SalesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.onError
import network.onSuccess
import storage.SessionHandler

class InvoiceViewModel(
    private val sessionHandler: SessionHandler,
    private val salesRepository: SalesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InvoiceUiState())
    val uiState: StateFlow<InvoiceUiState> = _uiState

    fun onEvent(event: InvoiceUiEvent) {
        when (event) {
            is InvoiceUiEvent.ArgumentPaymentLoaded -> {
                handlePaymentLoaded(event)
            }

            is InvoiceUiEvent.ArgumentNoInvoiceLoaded -> {
                getInvoice(event.noInvoice)
            }
        }
    }

    private fun getInvoice(noInvoice: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.getInvoice(noInvoice)
            withContext(Dispatchers.Main) {
                result.onSuccess { invoiceData ->
                    if (invoiceData.code == "200") {
                        _uiState.value = _uiState.value.copy(
                            totalHarga = invoiceData.data.detil.sumOf { it.subtotal.toInt() }
                                .toString()
                                .toDoubleOrNull() ?: 0.0,
                            diskon = invoiceData.data.detil.sumOf { it.diskon.toInt() }
                                .toString()
                                .toDoubleOrNull() ?: 0.0,
                            subtotal = _uiState.value.totalHarga - _uiState.value.diskon,
                            invoiceNumber = invoiceData.data.invoice,
                            tanggal = invoiceData.data.tanggal,
                            method = invoiceData.data.method,
                            kasir = invoiceData.data.kasir,
                            ppn = invoiceData.data.ppn.toDoubleOrNull() ?: 0.0,
                            bayar = invoiceData.data.bayar.toDoubleOrNull() ?: 0.0,
                            kembali = invoiceData.data.kembali.toDoubleOrNull() ?: 0.0,
                            detil = invoiceData.data.detil,
                            store = getStoreInfo()
                        )
                    }
                }.onError {
                    _uiState.value = _uiState.value.copy(errorMessage = it)
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun handlePaymentLoaded(event: InvoiceUiEvent.ArgumentPaymentLoaded) {
        viewModelScope.launch {
            val totalHarga = event.payment.data.detil.sumOf { it.subtotal.toInt() }.toString()
                .toDoubleOrNull() ?: 0.0
            val diskon = event.payment.data.detil.sumOf { it.diskon.toInt() }.toString()
                .toDoubleOrNull() ?: 0.0
            _uiState.value = _uiState.value.copy(
                totalHarga = totalHarga,
                diskon = diskon,
                subtotal = totalHarga - diskon,
                invoiceNumber = event.payment.data.invoice,
                tanggal = event.payment.data.tanggal,
                method = event.payment.data.method,
                kasir = event.payment.data.kasir,
                ppn = event.payment.data.ppn.toDoubleOrNull() ?: 0.0,
                bayar = event.payment.data.bayar.toDoubleOrNull() ?: 0.0,
                kembali = event.payment.data.kembali.toDoubleOrNull() ?: 0.0,
                detil = event.payment.data.detil,
                store = getStoreInfo()
            )
        }
    }

    private suspend fun getStoreInfo(): Toko {
        return Toko(
            nama = sessionHandler.getStoreName().firstOrNull() ?: "Unknown Store",
            alamat = sessionHandler.getAddress().firstOrNull() ?: "Unknown Address",
            telp = sessionHandler.getTelp().firstOrNull() ?: "Unknown Phone"
        )
    }
}