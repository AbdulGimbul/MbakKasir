package ui.navigation.tab.cashier_role

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import features.auth.data.AuthRepository
import features.auth.presentation.LoginScreen
import mbakkasir.composeapp.generated.resources.Res
import mbakkasir.composeapp.generated.resources.ic_user
import network.createHttpClient
import org.jetbrains.compose.resources.painterResource

object ProfileTab : Tab {
    @Composable
    override fun Content() {
//        LoginScreen().Content()
    }

    override val options: TabOptions
        @Composable
        get() {
            val icon = painterResource(Res.drawable.ic_user)
            return remember {
                TabOptions(
                    index = 0u,
                    title = "Profil",
                    icon = icon
                )
            }
        }
}