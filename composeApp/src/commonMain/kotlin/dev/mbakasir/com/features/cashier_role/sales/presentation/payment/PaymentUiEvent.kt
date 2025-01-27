package dev.mbakasir.com.features.cashier_role.sales.presentation.payment


sealed class PaymentUiEvent {
    data class UangDiterimaChanged(val uangDiterima: String) : PaymentUiEvent()
    data class DeleteScannedProducts(val draftId: String) : PaymentUiEvent()
    data object DateIconClicked : PaymentUiEvent()
    data object ConfirmButtonClicked : PaymentUiEvent()
    data class ArgumentProductsLoaded(val products: List<dev.mbakasir.com.features.cashier_role.sales.domain.ProductTransSerializable>) :
        PaymentUiEvent()

    data class PaymentMethodChanged(val method: String) : PaymentUiEvent()
    data class NoInvoiceChanged(val noInvoice: String) : PaymentUiEvent()
    data class SelectedDateChanged(val date: String) : PaymentUiEvent()
    data class DraftIsPrinted(val draftId: String) : PaymentUiEvent()
    data object DismissDialog : PaymentUiEvent()
}