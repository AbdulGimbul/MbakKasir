package features.cashier_role.sales.presentation.entry_sales

import features.cashier_role.sales.domain.ProductTrans

sealed class EntrySalesUiEvent {
    data class OnInputUserChanged(val inputUser: String) : EntrySalesUiEvent()
    data class OnTotalTagihanChanged(val totalTagihan: Int) : EntrySalesUiEvent()
    data class ScanProduct(val barcode: String) : EntrySalesUiEvent()
    data object SearchProduct : EntrySalesUiEvent()
    data object ScanIconClick : EntrySalesUiEvent()
    data object FlashLightClick : EntrySalesUiEvent()
    data class OnLaunchGallery(val launchGallery: Boolean) : EntrySalesUiEvent()
    data class IncreaseProductQty(val product: ProductTrans) : EntrySalesUiEvent()
    data class DecreaseProductQty(val product: ProductTrans) : EntrySalesUiEvent()
}