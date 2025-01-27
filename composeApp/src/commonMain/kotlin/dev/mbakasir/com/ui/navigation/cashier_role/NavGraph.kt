package dev.mbakasir.com.ui.navigation.cashier_role

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Domain
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.window.core.layout.WindowWidthSizeClass
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SetupNavHost(navController: NavHostController, windowSize: WindowWidthSizeClass) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigationType: MbakKasirNavigationType = when (windowSize) {
        WindowWidthSizeClass.COMPACT -> MbakKasirNavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.EXPANDED -> MbakKasirNavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> MbakKasirNavigationType.BOTTOM_NAVIGATION
    }

    when (navigationType) {
        MbakKasirNavigationType.BOTTOM_NAVIGATION -> {
            Scaffold(
                bottomBar = {
                    if (currentRoute in listOf(
                            Screen.Home.route,
                            Screen.Sales.route,
                            Screen.Product.route,
                            Screen.Profile.route,
                        )
                    ) {
                        BottomBar(navController)
                    }
                },
                floatingActionButton = {
                    if (currentRoute == Screen.Sales.route) {
                        val draftId = dev.mbakasir.com.utils.generateKodeInvoice()
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("${Screen.EntrySales.route}/$draftId")
                            },
                            shape = CircleShape,
                            containerColor = dev.mbakasir.com.ui.theme.primary
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
                NavHostContent(
                    navController = navController,
                    innerPadding = innerPadding,
                    navigationType = navigationType
                )
            }
        }

        MbakKasirNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationDrawer(
                drawerContent = { SideBar(navController) },
                content = {
                    Scaffold(
                        floatingActionButton = {
                            if (currentRoute == Screen.Sales.route) {
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate(Screen.EntrySales.route)
                                    },
                                    shape = CircleShape,
                                    containerColor = dev.mbakasir.com.ui.theme.primary
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
                        NavHostContent(
                            navController = navController,
                            innerPadding = innerPadding,
                            navigationType = navigationType
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun NavHostContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
    navigationType: MbakKasirNavigationType
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screen.Login.route) {
            dev.mbakasir.com.features.auth.presentation.login.LoginScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.auth.presentation.login.LoginViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Home.route) {
            dev.mbakasir.com.features.cashier_role.home.presentation.HomeScreen(viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.home.presentation.HomeViewModel>())
        }
        composable(Screen.Product.route){
            dev.mbakasir.com.features.cashier_role.product.presentation.ProductScreen(viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.product.presentation.ProductViewModel>())
        }
        composable(Screen.Sales.route) {
            dev.mbakasir.com.features.cashier_role.sales.SalesScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.SalesViewModel>(),
                navController = navController
            )
        }
        composable(Screen.History.route) {
            dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Profile.route) {
            dev.mbakasir.com.features.auth.presentation.profile.ProfileScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.auth.presentation.profile.ProfileViewModel>(),
                navController = navController
            )
        }
        composable(
            route = "${Screen.EntrySales.route}/{draftId}",
        ) { backStackEntry ->
            val draftId = backStackEntry.arguments?.getString("draftId")
            dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel>(),
                navController = navController,
                paymentViewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentViewModel>(),
                navigationType = navigationType,
                draftId = draftId.toString()
            )
        }
        composable(route = "${Screen.Payment.route}/?scannedProducts={scannedProducts}&draftId={draftId}",
            arguments = listOf(
                navArgument("scannedProducts") { nullable = false },
                navArgument("draftId") { nullable = false }
            )) { backStackEntry ->
            val draftId = backStackEntry.arguments?.getString("draftId")
            val jsonResponse = backStackEntry.arguments?.getString("scannedProducts")
            val scannedProducts =
                jsonResponse?.let { Json.decodeFromString<List<dev.mbakasir.com.features.cashier_role.sales.domain.ProductTransSerializable>>(it) }
            dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentViewModel>(),
                navController = navController,
                products = scannedProducts ?: emptyList(),
                draftId = draftId.toString()
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
                jsonResponse?.let { Json.decodeFromString<dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState>(it) }
            dev.mbakasir.com.features.cashier_role.sales.presentation.invoice.InvoiceScreen(
                viewModel = koinViewModel<dev.mbakasir.com.features.cashier_role.sales.presentation.invoice.InvoiceViewModel>(),
                paymentData = paymentData,
                noInvoice = noInvoice,
                navController = navController
            )
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

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
                    selectedIconColor = dev.mbakasir.com.ui.theme.primary,
                    selectedTextColor = dev.mbakasir.com.ui.theme.primary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun SideBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    PermanentDrawerSheet(
        modifier = Modifier
            .width(240.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .padding(24.dp)
        ) {
            navigationItems.forEach { item ->
                NavigationDrawerItem(
                    selected = currentRoute == item.screen.route,
                    label = {
                        Text(text = item.title)
                    },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                    onClick = {
                        navController.navigate(item.screen.route) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(Screen.Home.route) { saveState = true }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

enum class MbakKasirNavigationType {
    BOTTOM_NAVIGATION,
    PERMANENT_NAVIGATION_DRAWER
}

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
        title = "Barang",
        icon = Icons.Outlined.Domain,
        screen = Screen.Product
    ),
    BottomRailNavItem(
        title = "Akun",
        icon = Icons.Outlined.AccountCircle,
        screen = Screen.Profile
    )
)