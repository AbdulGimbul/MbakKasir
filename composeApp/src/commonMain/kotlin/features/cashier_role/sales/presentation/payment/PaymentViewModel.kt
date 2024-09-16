package features.cashier_role.sales.presentation.payment

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.plusmobileapps.konnectivity.Konnectivity
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
import kotlinx.coroutines.withContext
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
    private val _connectivity = MutableStateFlow(Konnectivity())
    val connectivity: StateFlow<Konnectivity> = _connectivity

    fun createPayment(paymentRequest: CreatePaymentRequest) {
        _isLoading.value = true
        _errorMessage.value = null

        screenModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(paymentRequest)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
                        _store.value = Toko(
                            nama = sessionHandler.getStoreName().first(),
                            alamat = sessionHandler.getAddress().first(),
                            telp = sessionHandler.getTelp().first()
                        )
                        _paymentResponse.value = it
                    }
                }.onError {
                    _errorMessage.value = it
                }

                _isLoading.value = false
            }
        }
    }

    fun deleteScannedProducts(productId: String) {
        screenModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteProductTrans(productId)
        }
    }
}