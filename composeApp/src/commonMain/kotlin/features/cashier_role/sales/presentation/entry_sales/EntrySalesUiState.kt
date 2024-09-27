package features.cashier_role.sales.presentation.entry_sales

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.ProductTrans

data class EntrySalesUiState(
    val inputUser: String = "",
    val barcode: String = "",
    val flashlightOn: Boolean = false,
    val launchGallery: Boolean = false,
    val totalTagihan: Int = 0,
    val product: ProductTrans = ProductTrans(),
    val searchResults: List<Product> = emptyList(),
    val scannedProducts: List<ProductTrans> = emptyList(),
    val startBarCodeScan: Boolean = false,
    val errorMessage: String? = null,
)