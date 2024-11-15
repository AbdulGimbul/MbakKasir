package features.cashier_role.sales.presentation.invoice

import features.cashier_role.sales.presentation.payment.PaymentUiState

sealed class InvoiceUiEvent {
    data class ArgumentPaymentLoaded(val payment: PaymentUiState) : InvoiceUiEvent()
    data class ArgumentNoInvoiceLoaded(val noInvoice: String) : InvoiceUiEvent()
}