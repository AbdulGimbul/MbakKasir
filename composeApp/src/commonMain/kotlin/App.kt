import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.cashier_role.SetupNavHost
import ui.theme.PoppinsTypography

@Composable
@Preview
fun App() {
    MaterialTheme(
        typography = PoppinsTypography(),
    ) {
        val navController = rememberNavController()
        SetupNavHost(navController)
    }
}