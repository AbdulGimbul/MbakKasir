package features.cashier_role.sales

sealed class SalesUiEvent {
    data class SendDraftTrans(val invoiceNumber: String) : SalesUiEvent()
}