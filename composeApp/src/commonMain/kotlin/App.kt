import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import features.auth.data.AuthRepository
import features.auth.presentation.LoginScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import storage.SessionHandler
import ui.theme.PoppinsTypography

@Composable
@Preview
fun App(sessionHandler: SessionHandler, authRepository: AuthRepository) {
    MaterialTheme(
        typography = PoppinsTypography(),
    ) {
        Navigator(LoginScreen(sessionHandler = sessionHandler, authRepository = authRepository))
    }
}