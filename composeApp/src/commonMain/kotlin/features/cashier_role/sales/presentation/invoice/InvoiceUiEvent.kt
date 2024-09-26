package features.cashier_role.sales.presentation.invoice

import features.cashier_role.sales.domain.CreatePaymentApiModel

sealed class InvoiceUiEvent {
    data class ArgumentPaymentLoaded(val payment: CreatePaymentApiModel) : InvoiceUiEvent()
    data class ArgumentNoInvoiceLoaded(val noInvoice: String) : InvoiceUiEvent()
}