import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import features.auth.presentation.LoginScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.theme.PoppinsTypography

@Composable
@Preview
fun App() {
    MaterialTheme(
        typography = PoppinsTypography(),
    ) {
        Navigator(LoginScreen())
    }
}