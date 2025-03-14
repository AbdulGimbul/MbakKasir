package dev.mbakasir.com.features.admin_role.stock_opname.presentation.entry_stock_opname

import dev.mbakasir.com.features.admin_role.stock_opname.domain.ProductStockOpname
import dev.mbakasir.com.features.admin_role.stock_opname.domain.UpdateStockOpnameApiModel
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity

data class EntryStockOpnameUiState(
    val inputUser: String = "",
    val barcode: String = "",
    val flashlightOn: Boolean = false,
    val launchGallery: Boolean = false,
    val product: ProductStockOpname = ProductStockOpname(),
    val updateStockResponse: UpdateStockOpnameApiModel? = null,
    val searchResults: List<ProductEntity> = emptyList(),
    val startBarCodeScan: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)