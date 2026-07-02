package dev.mbakasir.com.utils

import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterCurrencyStyle
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun currencyFormat(value: Double): String {
    val formatter =
        NSNumberFormatter().apply {
            numberStyle = NSNumberFormatterCurrencyStyle
            locale = NSLocale("in_ID")
        }
    return formatter.stringFromNumber(NSNumber(double = value)) ?: "$value"
}

actual interface JavaSerializable

class IosBrowserHelper : BrowserHelper {
    override fun openBrowser(url: String) {
        val nsUrl = NSURL.URLWithString(url) ?: return
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

actual fun getBrowserHelper(): BrowserHelper = IosBrowserHelper()
