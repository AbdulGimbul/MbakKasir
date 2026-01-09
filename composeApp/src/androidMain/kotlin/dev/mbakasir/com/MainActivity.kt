package dev.mbakasir.com

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dev.mbakasir.com.android.AppUpdateManagerWrapper
import network.chaintech.composeMultiplatformScreenCapture.AppContext

class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManagerWrapper: AppUpdateManagerWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appUpdateManagerWrapper = AppUpdateManagerWrapper(this)
        appUpdateManagerWrapper.checkForUpdate()

        AppContext.apply { set(this@MainActivity) }
        installSplashScreen()
        setContent { App() }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManagerWrapper.onResume()
    }
}
