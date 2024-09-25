package ui.navigation.cashier_role

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import features.auth.presentation.LoginScreen
import features.auth.presentation.ProfileScreen
import features.cashier_role.history.presentation.HistoryScreen
import features.cashier_role.home.presentation.HomeScreen
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.ProductTransSerializable
import features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen
import features.cashier_role.sales.presentation.payment.InvoiceScreen
import features.cashier_role.sales.presentation.payment.PaymentScreen
import features.cashier_role.sales.presentation.sales.SalesScreen
import kotlinx.serialization.json.Json
import ui.theme.primary

@Composable
fun SetupNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf(
                    Screen.Home.route,
                    Screen.History.route,
                    Screen.Profile.route,
                    Screen.Sales.route
                )
            ) {
                BototmBar(navController)
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Sales.route) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Screen.EntrySales.route)
                    },
                    shape = CircleShape,
                    containerColor = primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    navigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Sales.route) {
                SalesScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    navigateToInvoice = {
                        navController.navigate("${Screen.Invoice.route}?noInvoice=$it")
                    }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
            composable(Screen.EntrySales.route) {
                EntrySalesScreen(
                    navigateBack = {
                        navController.navigateUp()
                    },
                    navigateToPayment = {
                        navController.navigate("${Screen.Payment.route}/$it")
                    }
                )
            }
            composable("${Screen.Payment.route}/{scannedProducts}") { backStackEntry ->
                val jsonResponse = backStackEntry.arguments?.getString("scannedProducts")
                val scannedProducts =
                    jsonResponse?.let { Json.decodeFromString<List<ProductTransSerializable>>(it) }
                PaymentScreen(
                    products = scannedProducts ?: emptyList(),
                    navigateToInvoice = {
                        navController.navigate("${Screen.Invoice.route}?paymentData=$it")
                    },
                    navigateBack = {
                        navController.navigateUp()
                    }
                )
            }
            composable(
                route = "${Screen.Invoice.route}?paymentData={paymentData}&noInvoice={noInvoice}",
                arguments = listOf(
                    navArgument("paymentData") { nullable = true },
                    navArgument("noInvoice") { nullable = true }
                )
            ) { backStackEntry ->
                val jsonResponse = backStackEntry.arguments?.getString("paymentData")
                val noInvoice = backStackEntry.arguments?.getString("noInvoice")
                val paymentData =
                    jsonResponse?.let { Json.decodeFromString<CreatePaymentApiModel>(it) }
                InvoiceScreen(
                    paymentResponse = paymentData,
                    noInvoice = noInvoice,
                    navigateBack = {
                        navController.navigate(Screen.Sales.route) {
                            popUpTo(Screen.Sales.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun BototmBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            BottomRailNavItem(
                title = "Beransda",
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            BottomRailNavItem(
                title = "Penjualan",
                icon = Icons.Default.ShoppingCart,
                screen = Screen.Sales
            ),
            BottomRailNavItem(
                title = "Histori",
                icon = Icons.Default.BarChart,
                screen = Screen.History
            ),
            BottomRailNavItem(
                title = "Profil",
                icon = Icons.Default.AccountCircle,
                screen = Screen.Profile
            )
        )

        navigationItems.map { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(text = item.title)
                }
            )
        }
    }
}