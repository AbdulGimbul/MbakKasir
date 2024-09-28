package ui.navigation.cashier_role

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import features.auth.presentation.login.LoginScreen
import features.auth.presentation.login.LoginViewModel
import features.auth.presentation.profile.ProfileScreen
import features.auth.presentation.profile.ProfileViewModel
import features.cashier_role.history.presentation.HistoryScreen
import features.cashier_role.history.presentation.HistoryViewModel
import features.cashier_role.home.presentation.HomeScreen
import features.cashier_role.home.presentation.HomeViewModel
import features.cashier_role.sales.SalesScreen
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.ProductTransSerializable
import features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen
import features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel
import features.cashier_role.sales.presentation.invoice.InvoiceScreen
import features.cashier_role.sales.presentation.invoice.InvoiceViewModel
import features.cashier_role.sales.presentation.payment.PaymentScreen
import features.cashier_role.sales.presentation.payment.PaymentViewModel
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
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
                    viewModel = koinViewModel<LoginViewModel>(),
                    navController = navController
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(viewModel = koinViewModel<HomeViewModel>())
            }
            composable(Screen.Sales.route) {
                SalesScreen()
            }
            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = koinViewModel<HistoryViewModel>(),
                    navController = navController
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(viewModel = koinViewModel<ProfileViewModel>())
            }
            composable(Screen.EntrySales.route) {
                EntrySalesScreen(
                    viewModel = koinViewModel<EntrySalesViewModel>(),
                    navController = navController,
                )
            }
            composable("${Screen.Payment.route}/{scannedProducts}") { backStackEntry ->
                val jsonResponse = backStackEntry.arguments?.getString("scannedProducts")
                val scannedProducts =
                    jsonResponse?.let { Json.decodeFromString<List<ProductTransSerializable>>(it) }
                PaymentScreen(
                    viewModel = koinViewModel<PaymentViewModel>(),
                    navController = navController,
                    products = scannedProducts ?: emptyList()
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
                    viewModel = koinViewModel<InvoiceViewModel>(),
                    paymentResponse = paymentData,
                    noInvoice = noInvoice,
                    navController = navController
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
                title = "Beranda",
                icon = Icons.Outlined.Home,
                screen = Screen.Home
            ),
            BottomRailNavItem(
                title = "Penjualan",
                icon = Icons.Outlined.ShoppingCart,
                screen = Screen.Sales
            ),
            BottomRailNavItem(
                title = "Histori",
                icon = Icons.Outlined.BarChart,
                screen = Screen.History
            ),
            BottomRailNavItem(
                title = "Akun",
                icon = Icons.Outlined.AccountCircle,
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
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = primary,
                    selectedTextColor = primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}