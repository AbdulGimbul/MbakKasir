package dev.mbakasir.com.ui.navigation.admin_role

sealed class AdminScreen(val route: String) {
    data object Home : AdminScreen("home")
    data object Profile : AdminScreen("profile")
    data object StockInOut: AdminScreen("stock_in_out")
    data object StockOpname: AdminScreen("stock_opname")
    data object StockOpnamePreview: AdminScreen("stock_opname_preview")
    data object EntryStockOpname: AdminScreen("entry_stock_opname")

}