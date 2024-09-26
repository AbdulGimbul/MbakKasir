package features.cashier_role.sales.presentation.payment

import features.cashier_role.sales.domain.ProductTransSerializable

sealed class PaymentUiEvent {
    data class UangDiterimaChanged(val uangDiterima: String) : PaymentUiEvent()
    data class DeleteScannedProducts(val idBarang: String) : PaymentUiEvent()
    data object DateIconClicked : PaymentUiEvent()
    data class ConfirmButtonClicked(val method: String) : PaymentUiEvent()
    data class ArgumentProductsLoaded(val products: List<ProductTransSerializable>) :
        PaymentUiEvent()

    data class SelectedDateChanged(val date: String) : PaymentUiEvent()
    data object DismissDialog : PaymentUiEvent()
}