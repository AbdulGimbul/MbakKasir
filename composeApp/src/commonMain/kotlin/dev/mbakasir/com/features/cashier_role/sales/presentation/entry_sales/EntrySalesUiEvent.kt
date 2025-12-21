package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales

import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity

sealed class EntrySalesUiEvent {
    data class OnInputUserChanged(val inputUser: String) : EntrySalesUiEvent()
    data class OnTotalsChanged(val totalHarga: Int, val totalDiskon: Int, val totalTagihan: Int) : EntrySalesUiEvent()
    data class ScanProduct(val draftId: String, val barcode: String) : EntrySalesUiEvent()
    data object SearchProduct : EntrySalesUiEvent()
    data object ScanIconClick : EntrySalesUiEvent()
    data object FlashLightClick : EntrySalesUiEvent()
    data class OnLaunchGallery(val launchGallery: Boolean) : EntrySalesUiEvent()
    data class IncreaseProductQty(val draftId: String, val product: ProductTransEntity) :
        EntrySalesUiEvent()

    data class DecreaseProductQty(val draftId: String, val product: ProductTransEntity) :
        EntrySalesUiEvent()

    data class DeleteProduct(val draftId: String) : EntrySalesUiEvent()
    data class LoadScannedProducts(val draftId: String) : EntrySalesUiEvent()
    data class OnSearchCustChanged(val searchCust: String) : EntrySalesUiEvent()
    data class OnCustomerCheckChanged(val checked: Boolean) : EntrySalesUiEvent()
}
