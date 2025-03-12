package dev.mbakasir.com.ui.navigation.admin_role

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
import androidx.window.core.layout.WindowWidthSizeClass
import dev.mbakasir.com.features.admin_role.product.EntryStockOpnameScreen
import dev.mbakasir.com.features.admin_role.product.StockOpnamePreviewScreen
import dev.mbakasir.com.features.admin_role.stock_opname.presentation.StockOpnameScreen
import dev.mbakasir.com.features.admin_role.stock_opname.presentation.StockOpnameViewModel
import dev.mbakasir.com.features.auth.presentation.profile.ProfileScreen
import dev.mbakasir.com.features.auth.presentation.profile.ProfileViewModel
import dev.mbakasir.com.ui.theme.primary
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AdminNavHost(
    navController: NavHostController,
    windowSize: WindowWidthSizeClass,
    parentNavController: NavHostController,
) {
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
                    BottomBar(navController)
                },
                floatingActionButton = {
                    if (currentRoute == AdminScreen.StockOpname.route) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(AdminScreen.EntryStockOpname.route)
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
                    navigationType = navigationType,
                    parentNavController = parentNavController
                )
            }
        }

        MbakKasirNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            PermanentNavigationDrawer(
                drawerContent = { SideBar(navController) },
                content = {
                    Scaffold(
                        floatingActionButton = {
                            if (currentRoute == AdminScreen.StockOpname.route) {
                                FloatingActionButton(
                                    onClick = {
                                        navController.navigate(AdminScreen.EntryStockOpname.route)
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
                            navigationType = navigationType,
                            parentNavController = parentNavController
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
    navigationType: MbakKasirNavigationType,
    parentNavController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AdminScreen.StockOpname.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(AdminScreen.Home.route) {
            EntryStockOpnameScreen()
        }
        composable(AdminScreen.Profile.route) {
            ProfileScreen(
                viewModel = koinViewModel<ProfileViewModel>(),
                navController = parentNavController
            )
        }
        composable(AdminScreen.StockInOut.route) {
            StockOpnamePreviewScreen()
        }
        composable(AdminScreen.StockOpname.route) {
            StockOpnameScreen(viewModel = koinViewModel<StockOpnameViewModel>(), navController = navController)
        }
        composable(AdminScreen.EntryStockOpname.route) {
            EntryStockOpnameScreen()
        }
        composable(AdminScreen.StockOpnamePreview.route) {
            StockOpnamePreviewScreen()
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
                            popUpTo(AdminScreen.Home.route) {
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
                                popUpTo(AdminScreen.Home.route) { saveState = true }
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
    AdminBottomRailNavItem(
        title = "Beranda",
        icon = Icons.Outlined.Home,
        screen = AdminScreen.EntryStockOpname
    ),
    AdminBottomRailNavItem(
        title = "Stock In/Out",
        icon = Icons.Outlined.ShoppingCart,
        screen = AdminScreen.StockOpnamePreview
    ),
    AdminBottomRailNavItem(
        title = "Stock Opname",
        icon = Icons.Outlined.Domain,
        screen = AdminScreen.StockOpname
    ),
    AdminBottomRailNavItem(
        title = "Akun",
        icon = Icons.Outlined.AccountCircle,
        screen = AdminScreen.Profile
    )
)