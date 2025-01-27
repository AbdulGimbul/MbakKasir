package dev.mbakasir.com.features.cashier_role.sales.presentation.invoice

sealed class InvoiceUiEvent {
    data class ArgumentPaymentLoaded(val payment: dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState) : InvoiceUiEvent()
    data class ArgumentNoInvoiceLoaded(val noInvoice: String) : InvoiceUiEvent()
}