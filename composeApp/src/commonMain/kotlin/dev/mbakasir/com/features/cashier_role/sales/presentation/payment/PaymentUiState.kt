package dev.mbakasir.com.features.cashier_role.sales.presentation.payment

import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.Customer
import dev.mbakasir.com.features.cashier_role.sales.domain.ProductTransSerializable
import kotlinx.serialization.Serializable

@Serializable
data class PaymentUiState(
    val products: List<ProductTransSerializable> = emptyList(),
    val uangDiterima: String = "",
    val selectedDate: String = "",
    val searchCust: String = "",
    val totalHarga: Int = 0,
    val diskon: Int = 0,
    val subtotal: Int = 0,
    val kembalian: Int = 0,
    val showDatePicker: Boolean = false,
    val isConnected: Boolean = true,
    val paymentMethod: String = "",
    val noInvoice: String = "",
    val currentDate: String = "",
    val customers: List<Customer> = emptyList(),
    val paymentResponse: CreatePaymentApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)