package features.cashier_role.sales.presentation.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plusmobileapps.konnectivity.Konnectivity
import features.cashier_role.sales.data.SalesRepository
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import network.NetworkError
import network.onError
import network.onSuccess

class PaymentViewModel(private val salesRepository: SalesRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow<NetworkError?>(null)
    val errorMessage: StateFlow<NetworkError?> = _errorMessage
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    private val _paymentResponse = MutableStateFlow<CreatePaymentApiModel?>(null)
    val paymentResponse: StateFlow<CreatePaymentApiModel?> = _paymentResponse
    private val _connectivity = MutableStateFlow(Konnectivity())
    val connectivity: StateFlow<Konnectivity> = _connectivity

    fun createPayment(paymentRequest: CreatePaymentRequest) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch(Dispatchers.IO) {
            val result = salesRepository.createPayment(paymentRequest)
            withContext(Dispatchers.Main) {
                result.onSuccess {
                    if (it.code == "200") {
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
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.deleteProductTrans(productId)
        }
    }
}