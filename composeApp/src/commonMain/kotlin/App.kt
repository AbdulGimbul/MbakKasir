import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import features.admin_role.product.StockOpnamePreviewScreen
import features.admin_role.product.StockOpnameScreen
import features.cashier_role.EntrySalesScreen
import features.cashier_role.PaymentScreen
import features.cashier_role.ReceiptScreen
import mbakkasir.composeapp.generated.resources.Poppins_Regular
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.tab.cashier_role.HistoryTab
import ui.navigation.tab.cashier_role.HomeTab
import ui.navigation.tab.cashier_role.ProfileTab
import ui.navigation.tab.cashier_role.SalesTab
import ui.theme.icon
import ui.theme.primary

@Composable
@Preview
fun App() {
    MaterialTheme(
//        typography = Typography(
//            defaultFontFamily = PoppinsFontFamily()
//        )
    ) {
//        TabNavigator(HomeTab) {
//            val currentTab = LocalTabNavigator.current.current
//            Scaffold(
//                bottomBar = {
//                    BottomNavigation(
//                        backgroundColor = Color.White
//                    ) {
//                        TabNavigationItem(HomeTab)
//                        TabNavigationItem(SalesTab)
//                        TabNavigationItem(HistoryTab)
//                        TabNavigationItem(ProfileTab)
//                    }
//                },
//                floatingActionButton = {
//                    if (currentTab == SalesTab) {
//                        FloatingActionButton(onClick = { /* TODO: Add action */ }) {
//                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
//                        }
//                    }
//                },
//                modifier = Modifier.statusBarsPadding().navigationBarsPadding()
//            ) {
//                CurrentTab()
//            }
//        }
        StockOpnamePreviewScreen()
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = {
            Text(
                tab.options.title,
                fontFamily = FontFamily(Font(Res.font.Poppins_Regular))
            )
        },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        },
        selectedContentColor = primary,
        unselectedContentColor = icon
    )
}