package ui.navigation.cashier_role

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Sales : Screen("sales")
    data object History : Screen("history")
    data object Profile : Screen("profile")
    data object Login : Screen("login")
    data object EntrySales : Screen("entry_sales")
    data object Payment : Screen("payment")
    data object Invoice : Screen("invoice")
    data object Product : Screen("product")
}