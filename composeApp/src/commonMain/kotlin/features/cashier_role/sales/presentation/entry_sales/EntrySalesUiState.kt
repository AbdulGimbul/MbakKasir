package features.cashier_role.sales.presentation.entry_sales

import features.cashier_role.product.data.ProductEntity
import features.cashier_role.sales.data.ProductTransEntity

data class EntrySalesUiState(
    val inputUser: String = "",
    val barcode: String = "",
    val flashlightOn: Boolean = false,
    val launchGallery: Boolean = false,
    val totalTagihan: Int = 0,
    val product: ProductTransEntity = ProductTransEntity(),
    val searchResults: List<ProductEntity> = emptyList(),
    val scannedProducts: List<ProductTransEntity> = emptyList(),
    val startBarCodeScan: Boolean = false,
    val errorMessage: String? = null,
)