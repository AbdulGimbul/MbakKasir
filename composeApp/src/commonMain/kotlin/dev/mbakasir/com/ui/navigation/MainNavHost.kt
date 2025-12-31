package dev.mbakasir.com.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowWidthSizeClass
import dev.mbakasir.com.features.auth.presentation.login.LoginScreen
import dev.mbakasir.com.features.auth.presentation.login.LoginViewModel
import dev.mbakasir.com.storage.SessionHandler
import dev.mbakasir.com.ui.navigation.admin_role.AdminNavHost
import dev.mbakasir.com.ui.navigation.cashier_role.CashierNavHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainNavHost(navController: NavHostController, windowSize: WindowWidthSizeClass) {
    val sessionHandler: SessionHandler = koinInject()
    val token by sessionHandler.getToken().collectAsStateWithLifecycle("")

    LaunchedEffect(token) {
        if (token.isEmpty()) {
            val currentRoute = navController.currentDestination?.route
            if (currentRoute != MainScreen.Login.route && currentRoute != null) {
                navController.navigate(MainScreen.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = MainScreen.Login.route) {
        composable(MainScreen.Login.route) {
            LoginScreen(viewModel = koinViewModel<LoginViewModel>(), navController = navController)
        }

        composable(
                route = "${MainScreen.Cashier.route}/{role}",
                arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: ""
            CashierNavHost(
                    navController = rememberNavController(),
                    windowSize = windowSize,
                    parentNavController = navController,
                    role = role
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
