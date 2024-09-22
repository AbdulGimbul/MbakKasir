package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import features.admin_role.product.EntryStockOpnameScreen
import features.cashier_role.history.presentation.HistoryScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_document
import org.jetbrains.compose.resources.painterResource

object HistoryTab : Tab {
    @Composable
    override fun Content() {
        Navigator(HistoryScreen())
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_document)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Histori",
                    icon = icon
                )
            }
        }
}