package dev.mbakasir.com.ui.navigation

sealed class MainScreen(val route: String) {
    object Login : MainScreen("login")
    object Cashier : MainScreen("cashier")
    object Admin : MainScreen("admin")
}