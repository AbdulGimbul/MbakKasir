package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import features.admin_role.product.StockOpnameScreen
import features.cashier_role.SalesScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_cart
import org.jetbrains.compose.resources.painterResource

object SalesTab : Tab {
    @Composable
    override fun Content() {
        SalesScreen()
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