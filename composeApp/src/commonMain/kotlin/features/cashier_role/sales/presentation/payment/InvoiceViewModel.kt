package features.cashier_role.sales.presentation.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.auth.domain.Toko
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.InvoiceApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.NetworkError
import network.onError
import network.onSuccess
import storage.SessionHandler

class InvoiceViewModel(
    private val sessionHandler: SessionHandler,
    private val salesRepository: SalesRepository
) : ScreenModel {

    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _invoiceResponse = MutableStateFlow<InvoiceApiModel?>(null)
    val invoiceResponse: StateFlow<InvoiceApiModel?> = _invoiceResponse
    private val _store = MutableStateFlow<Toko?>(null)
    val store: StateFlow<Toko?> = _store

    init {
        getStoreInfo()
    }

    fun getInvoice(noInvoice: String) {
        _isLoading.value = true
        _errorMessage.value = null

        screenModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.getInvoice(noInvoice)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _invoiceResponse.value = it
                    }
                }.onError {
                    _errorMessage.value = it
                }

                _isLoading.value = false
            }
        }
    }

    private fun getStoreInfo() {
        screenModelScope.launch {
            _store.value = Toko(
                nama = sessionHandler.getStoreName().first(),
                alamat = sessionHandler.getAddress().first(),
                telp = sessionHandler.getTelp().first()
            )
        }
    }
}