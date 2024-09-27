package utils

actual fun currencyFormat(
    amount: Double
): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterCurrencyStyle
        locale = NSLocale("in_ID")
    }
    return formatter.stringFromNumber(amount) ?: "$amount"
}

actual interface JavaSerializable