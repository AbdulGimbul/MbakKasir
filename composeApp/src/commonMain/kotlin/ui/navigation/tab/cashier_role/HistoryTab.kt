package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import features.admin_role.product.EntryStockOpnameScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_document
import org.jetbrains.compose.resources.painterResource

object HistoryTab : Tab {
    @Composable
    override fun Content() {
        EntryStockOpnameScreen()
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