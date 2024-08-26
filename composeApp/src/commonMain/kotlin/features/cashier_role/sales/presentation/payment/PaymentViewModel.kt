package features.cashier_role.sales.presentation.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import features.auth.domain.Toko
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import network.NetworkError
import network.onError
import network.onSuccess
import storage.SessionHandler

class PaymentViewModel(
    private val sessionHandler: SessionHandler,
    private val salesRepository: SalesRepository
) : ScreenModel {
    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _paymentResponse = MutableStateFlow<CreatePaymentApiModel?>(null)
    val paymentResponse: StateFlow<CreatePaymentApiModel?> = _paymentResponse
    private val _store = MutableStateFlow<Toko?>(null)
    val store: StateFlow<Toko?> = _store

    fun createPayment(paymentRequest: CreatePaymentRequest) {
        _isLoading.value = true
        _errorMessage.value = null

        screenModelScope.launch(Dispatchers.IO) {
            salesRepository.createPayment(paymentRequest)
                .onSuccess {
                    if (it.code == "200") {
                        _paymentResponse.value = it
                        _store.value = Toko(
                            nama = sessionHandler.getStoreName().first(),
                            alamat = sessionHandler.getAddress().first(),
                            telp = sessionHandler.getTelp().first()
                        )
                    }
                }
                .onError {
                    _errorMessage.value = it
                }

            _isLoading.value = false
        }
    }
}