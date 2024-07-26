package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import features.cashier_role.HomeScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_house
import org.jetbrains.compose.resources.painterResource

object HomeTab : Tab {
    @Composable
    override fun Content() {
        HomeScreen()
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_house)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Beranda",
                    icon = icon
                )
            }
        }
}