package ui.navigation.screen

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import features.admin_role.product.EntryStockOpnameScreen
import features.cashier_role.HomeScreen

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        LoginScreen()
    }
}

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomeScreen()
    }
}

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        EntryStockOpnameScreen()
    }
}