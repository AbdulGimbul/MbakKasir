package dev.mbakasir.com.android

import androidx.activity.ComponentActivity
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

class AppUpdateManagerWrapper(private val activity: ComponentActivity) {

    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private val updateType = AppUpdateType.IMMEDIATE
    private val requestCode = 12345

    fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                            appUpdateInfo.isUpdateTypeAllowed(updateType)
            ) {
                // Request the update
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(updateType).build(),
                        requestCode
                )
            }
        }
    }

    fun processActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == this.requestCode) {
            when (resultCode) {
                android.app.Activity.RESULT_CANCELED -> {
                    // If the update is cancelled by the user, request it again.
                    // This forces the user to update.
                    checkForUpdate()
                }
                com.google.android.play.core.install.model.ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    // If the update failed, request it again or handle failure.
                    checkForUpdate()
                }
            }
        }
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() ==
                            UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
            ) {
                // If an in-app update is already in progress, resume the update.
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        activity,
                        AppUpdateOptions.newBuilder(updateType).build(),
                        requestCode
                )
            }
        }
    }
}
