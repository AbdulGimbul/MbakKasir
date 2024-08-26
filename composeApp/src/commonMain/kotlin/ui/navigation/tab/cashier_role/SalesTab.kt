package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import features.cashier_role.sales.presentation.sales.SalesScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_cart
import org.jetbrains.compose.resources.painterResource

object SalesTab : Tab {
    private var setNavigator: (Navigator) -> Unit = {}

    operator fun invoke(setNavigator: (Navigator) -> Unit): SalesTab {
        this.setNavigator = setNavigator
        return this
    }

    @Composable
    override fun Content() {
        Navigator(SalesScreen()) { navigator ->
            setNavigator(navigator)
            SlideTransition(navigator = navigator)
        }
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_cart)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Penjualan",
                    icon = icon
                )
            }
        }
}