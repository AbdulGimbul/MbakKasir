package dev.mbakasir.com.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.savedstate.read
import androidx.window.core.layout.WindowSizeClass
import dev.mbakasir.com.features.auth.presentation.login.LoginScreen
import dev.mbakasir.com.features.auth.presentation.login.LoginViewModel
import dev.mbakasir.com.storage.SessionHandler
import dev.mbakasir.com.ui.navigation.admin_role.AdminNavHost
import dev.mbakasir.com.ui.navigation.cashier_role.CashierNavHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

private const val NAV_ANIM_DURATION = 300

@Composable
fun MainNavHost(navController: NavHostController, windowSizeClass: WindowSizeClass) {
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

    NavHost(
        navController = navController,
        startDestination = MainScreen.Login.route,
        enterTransition = {
            fadeIn(animationSpec = tween(NAV_ANIM_DURATION)) +
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(NAV_ANIM_DURATION)
                )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(NAV_ANIM_DURATION)) +
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(NAV_ANIM_DURATION)
                )
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(NAV_ANIM_DURATION)) +
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_ANIM_DURATION)
                )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(NAV_ANIM_DURATION)) +
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(NAV_ANIM_DURATION)
                )
        }
    ) {
        composable(MainScreen.Login.route) {
            LoginScreen(viewModel = koinViewModel<LoginViewModel>(), navController = navController)
        }

        composable(
            route = "${MainScreen.Cashier.route}/{role}",
            arguments = listOf(navArgument("role") { type = NavType.StringType })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.read { getStringOrNull("role") } ?: ""
            CashierNavHost(
                navController = rememberNavController(),
                windowSizeClass = windowSizeClass,
                parentNavController = navController,
                role = role
            )
        }

        composable(MainScreen.Admin.route) {
            AdminNavHost(
                navController = rememberNavController(),
                windowSizeClass = windowSizeClass,
                parentNavController = navController
            )
        }
    }
}

