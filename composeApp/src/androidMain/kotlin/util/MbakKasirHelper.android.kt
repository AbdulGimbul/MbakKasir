package util

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