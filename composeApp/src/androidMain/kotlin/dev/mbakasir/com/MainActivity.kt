package dev.mbakasir.com

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.install.model.ActivityResult
import dev.mbakasir.com.android.AppUpdateManagerWrapper
import network.chaintech.composeMultiplatformScreenCapture.AppContext

class MainActivity : ComponentActivity() {

    private lateinit var appUpdateManagerWrapper: AppUpdateManagerWrapper

    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(StartIntentSenderForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    Log.d(TAG, "Update completed successfully")
                }

                RESULT_CANCELED -> {
                    // If the update is cancelled by the user, request it again.
                    // This forces the user to update.
                    Log.w(TAG, "Update cancelled by user, re-requesting")
                    appUpdateManagerWrapper.checkForUpdate()
                }

                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    // If the update failed, request it again or handle failure.
                    Log.e(TAG, "In-app update failed, re-requesting")
                    appUpdateManagerWrapper.checkForUpdate()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        appUpdateManagerWrapper = AppUpdateManagerWrapper(this, updateLauncher)
        appUpdateManagerWrapper.checkForUpdate()

        AppContext.apply { set(this@MainActivity) }
        installSplashScreen()
        setContent { App() }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManagerWrapper.onResume()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
