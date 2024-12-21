import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
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
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

        // For in case need some permission, full guide in stevdza-san moko-permission video
        /*
        val snackBarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }
        BindEffect(controller)
         */

        SetupNavHost(
            navController = navController,
            windowSize = windowSizeClass.windowWidthSizeClass
        )
    }
}

// For in case need some permission, full guide in stevdza-san moko-permission video
/*
suspend fun checkPermission(
    permission: Permission,
    controller: PermissionsController,
    snackbarHostState: SnackbarHostState
){
    val granted = controller.isPermissionGranted(permission)
    if (!granted){
        try {
            controller.providePermission(permission)
        } catch (e: DeniedException){
            val result = snackbarHostState.showSnackbar(
                message = "Denied",
                actionLabel = "Open Settings",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed){
                controller.openAppSettings()
            }
        } catch (e: DeniedAlwaysException){
            val result = snackbarHostState.showSnackbar(
                message = "Permanently denied",
                actionLabel = "Open Settings",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed){
                controller.openAppSettings()
            }
        } catch (e: RequestCanceledException){
            snackbarHostState.showSnackbar(
                message = "Request canceled."
            )
        }
    } else {
        snackbarHostState.showSnackbar(
            message = "Permission already granted."
        )
    }
}
*/