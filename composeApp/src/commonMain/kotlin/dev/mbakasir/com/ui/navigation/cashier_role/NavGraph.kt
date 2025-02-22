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
import dev.mbakasir.com.features.auth.presentation.login.LoginScreen
import dev.mbakasir.com.features.auth.presentation.login.LoginViewModel
import dev.mbakasir.com.features.auth.presentation.profile.ProfileScreen
import dev.mbakasir.com.features.auth.presentation.profile.ProfileViewModel
import dev.mbakasir.com.features.cashier_role.home.presentation.HomeScreen
import dev.mbakasir.com.features.cashier_role.home.presentation.HomeViewModel
import dev.mbakasir.com.features.cashier_role.product.presentation.ProductScreen
import dev.mbakasir.com.features.cashier_role.product.presentation.ProductViewModel
import dev.mbakasir.com.features.cashier_role.sales.SalesScreen
import dev.mbakasir.com.features.cashier_role.sales.SalesViewModel
import dev.mbakasir.com.features.cashier_role.sales.domain.ProductTransSerializable
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen
import dev.mbakasir.com.features.cashier_role.sales.presentation.entry_sales.EntrySalesViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryScreen
import dev.mbakasir.com.features.cashier_role.sales.presentation.history.HistoryViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.invoice.InvoiceScreen
import dev.mbakasir.com.features.cashier_role.sales.presentation.invoice.InvoiceViewModel
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentScreen
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentUiState
import dev.mbakasir.com.features.cashier_role.sales.presentation.payment.PaymentViewModel
import dev.mbakasir.com.ui.theme.primary
import dev.mbakasir.com.utils.generateKodeInvoice
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
                        val draftId = generateKodeInvoice()
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("${Screen.EntrySales.route}/$draftId")
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
            LoginScreen(
                viewModel = koinViewModel<LoginViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(viewModel = koinViewModel<HomeViewModel>())
        }
        composable(Screen.Product.route){
            ProductScreen(viewModel = koinViewModel<ProductViewModel>())
        }
        composable(Screen.Sales.route) {
            SalesScreen(
                viewModel = koinViewModel<SalesViewModel>(),
                navController = navController
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                viewModel = koinViewModel<HistoryViewModel>(),
                navController = navController
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = koinViewModel<ProfileViewModel>(),
                navController = navController
            )
        }
        composable(
            route = "${Screen.EntrySales.route}/{draftId}",
        ) { backStackEntry ->
            val draftId = backStackEntry.arguments?.getString("draftId")
            EntrySalesScreen(
                viewModel = koinViewModel<EntrySalesViewModel>(),
                navController = navController,
                paymentViewModel = koinViewModel<PaymentViewModel>(),
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
                jsonResponse?.let { Json.decodeFromString<List<ProductTransSerializable>>(it) }
            PaymentScreen(
                viewModel = koinViewModel<PaymentViewModel>(),
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
                jsonResponse?.let { Json.decodeFromString<PaymentUiState>(it) }
            InvoiceScreen(
                viewModel = koinViewModel<InvoiceViewModel>(),
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
                    selectedIconColor = primary,
                    selectedTextColor = primary,
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