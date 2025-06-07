package dev.mbakasir.com.features.admin_role.stock_opname.presentation

sealed class StockOpnameUiEvent {
    data object GetStockOpname : StockOpnameUiEvent()
    data class UpdateDate(val startDate: String, val endDate: String) : StockOpnameUiEvent()
}