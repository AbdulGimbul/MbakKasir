package dev.mbakasir.com.features.admin_role.stock_opname.presentation

import dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryUiEvent

sealed class StockOpnameUiEvent {
    data object GetStockOpname : StockOpnameUiEvent()
    data class UpdateDate(val startDate: String, val endDate: String) : StockOpnameUiEvent()
}