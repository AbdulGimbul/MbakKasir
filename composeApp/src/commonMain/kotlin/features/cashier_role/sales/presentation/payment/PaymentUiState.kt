package features.cashier_role.sales.presentation.payment

import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.ProductTransSerializable
import network.NetworkError

data class PaymentUiState(
    val products: List<ProductTransSerializable> = emptyList(),
    val uangDiterima: String = "",
    val selectedDate: String = "",
    val totalHarga: Int = 0,
    val diskon: Int = 0,
    val subtotal: Int = 0,
    val kembalian: Int = 0,
    val showDatePicker: Boolean = false,
    val isConnected: Boolean = true,
    val paymentResponse: CreatePaymentApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: NetworkError? = null,
)