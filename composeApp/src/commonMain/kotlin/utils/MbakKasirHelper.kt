package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


expect fun currencyFormat(amount: Double): String

expect interface JavaSerializable

fun generateKodeInvoice(): String {
    val currentMoment = Clock.System.now()
    val currentDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val formattedDateTime = currentDateTime.run {
        "${dayOfMonth.toString().padStart(2, '0')}${
            monthNumber.toString().padStart(2, '0')
        }${year.toString().takeLast(2)}" +
                "${hour.toString().padStart(2, '0')}${
                    minute.toString().padStart(2, '0')
                }${second.toString().padStart(2, '0')}"
    }

    return "POS$formattedDateTime"
}