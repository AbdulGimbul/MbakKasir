package dev.mbakasir.com.ui.navigation.cashier_role

sealed class CashierScreen(val route: String) {
    data object Home : CashierScreen("home")
    data object Sales : CashierScreen("sales")
    data object History : CashierScreen("history")
    data object Profile : CashierScreen("profile")
    data object EntrySales : CashierScreen("entry_sales")
    data object Payment : CashierScreen("payment")
    data object Invoice : CashierScreen("invoice")
    data object Product : CashierScreen("product")
}