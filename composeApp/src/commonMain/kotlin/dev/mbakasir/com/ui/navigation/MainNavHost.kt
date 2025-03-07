package dev.mbakasir.com.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import dev.mbakasir.com.features.auth.presentation.login.LoginScreen
import dev.mbakasir.com.features.auth.presentation.login.LoginViewModel
import dev.mbakasir.com.ui.navigation.admin_role.AdminNavHost
import dev.mbakasir.com.ui.navigation.cashier_role.CashierNavHost
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.Login.route
    ) {

        composable(MainScreen.Login.route) {
            LoginScreen(
                viewModel = koinViewModel<LoginViewModel>(),
                navController = navController
            )
        }

        composable(MainScreen.Cashier.route) {
            CashierNavHost(
                navController = rememberNavController(),
                windowSize = windowSize,
                parentNavController = navController
            )
        }

        composable(MainScreen.Admin.route) {
            AdminNavHost(
                navController = rememberNavController(),
                windowSize = windowSize,
                parentNavController = navController
            )
        }
    }
}