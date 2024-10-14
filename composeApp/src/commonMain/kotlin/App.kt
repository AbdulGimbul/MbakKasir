import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.cashier_role.SetupNavHost
import ui.theme.PoppinsTypography

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme(
        typography = PoppinsTypography(),
    ) {
        val navController = rememberNavController()
        val windowSizeClass = calculateWindowSizeClass()
        SetupNavHost(navController = navController, windowSize = windowSizeClass.widthSizeClass)
    }
}