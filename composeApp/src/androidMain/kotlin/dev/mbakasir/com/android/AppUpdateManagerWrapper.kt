package dev.mbakasir.com.android

import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateManagerWrapper(
    context: Context,
    private val updateLauncher: ActivityResultLauncher<IntentSenderRequest>
) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)
    private val updateType = AppUpdateType.IMMEDIATE

    fun checkForUpdate() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(updateType)
                ) {
                    Log.d(TAG, "Update available, starting update flow")
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(updateType).build()
                    )
                } else {
                    Log.d(TAG, "No update available or update type not allowed")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to check for update", exception)
            }
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() ==
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    Log.d(TAG, "Update in progress, resuming update flow")
                    // If an in-app update is already in progress, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(updateType).build()
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to check update status on resume", exception)
            }
    }

    companion object {
        private const val TAG = "AppUpdateManager"
    }
}
