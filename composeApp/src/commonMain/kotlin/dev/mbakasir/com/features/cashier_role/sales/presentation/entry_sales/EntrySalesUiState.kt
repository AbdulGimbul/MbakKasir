package dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales

import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity
import dev.mbakasir.com.features.cashier_role.sales.domain.Customer

data class EntrySalesUiState(
    val inputUser: String = "",
    val barcode: String = "",
    val flashlightOn: Boolean = false,
    val launchGallery: Boolean = false,
    val totalTagihan: Int = 0,
    val totalHarga: Int = 0,
    val totalDiskon: Int = 0,
    val product: ProductTransEntity = ProductTransEntity(),
    val searchResults: List<ProductEntity> = emptyList(),
    val scannedProducts: List<ProductTransEntity> = emptyList(),
    val startBarCodeScan: Boolean = false,
    val errorMessage: String? = null,
    val customers: List<Customer> = emptyList(),
    val checkedStatePelanggan: Boolean = false,
    val searchCust: String = "",
)
