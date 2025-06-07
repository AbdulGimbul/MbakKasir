package dev.mbakasir.com.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import dev.mbakasir.com.MyApplication
import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale

actual fun currencyFormat(
    amount: Double
): String {
    val indonesiaLocale = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(indonesiaLocale)
    return format.format(amount)
}

actual typealias JavaSerializable = Serializable

class AndroidBrowserHelper(private val context: Context) : BrowserHelper {
    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}

actual fun getBrowserHelper(): BrowserHelper = AndroidBrowserHelper(MyApplication.appContext)