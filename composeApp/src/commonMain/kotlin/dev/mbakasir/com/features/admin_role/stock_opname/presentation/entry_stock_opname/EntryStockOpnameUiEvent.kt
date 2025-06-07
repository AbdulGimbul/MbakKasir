package dev.mbakasir.com.features.admin_role.stock_opname.presentation.entry_stock_opname

sealed class EntryStockOpnameUiEvent {
    data class OnInputUserChanged(val inputUser: String) : EntryStockOpnameUiEvent()
    data class ScanProduct(val barcode: String) : EntryStockOpnameUiEvent()
    data object SearchProduct : EntryStockOpnameUiEvent()
    data class OnJumlahChanged(val jumlah: String) : EntryStockOpnameUiEvent()
    data class OnKeteranganChanged(val keterangan: String) : EntryStockOpnameUiEvent()
    data object ScanIconClick : EntryStockOpnameUiEvent()
    data object FlashLightClick : EntryStockOpnameUiEvent()
    data class OnLaunchGallery(val launchGallery: Boolean) : EntryStockOpnameUiEvent()
    data class LoadScannedProducts(val draftId: String) : EntryStockOpnameUiEvent()
    data object SubmitUpdateStock : EntryStockOpnameUiEvent()
}