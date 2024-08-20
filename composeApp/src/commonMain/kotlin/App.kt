import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import di.initKoin
import features.auth.data.AuthRepository
import features.auth.presentation.LoginScreen
import features.auth.presentation.LoginViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import storage.SessionHandler
import ui.theme.PoppinsTypography

@Composable
@Preview
fun App(viewModel: LoginViewModel) {
    MaterialTheme(
        typography = PoppinsTypography(),
    ) {
        Navigator(LoginScreen(viewModel))
    }
}