package utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
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

fun getCurrentFormattedDateTime(): String {
    val currentMoment = Clock.System.now()
    val dateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = dateTime.dayOfMonth.toString().padStart(2, '0')
    val month = dateTime.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val year = dateTime.year
    val hour = dateTime.hour.toString().padStart(2, '0')
    val minute = dateTime.minute.toString().padStart(2, '0')

    return "$day $month $year, $hour:$minute WIB"
}

fun currentTimeCustom(): String {
    val currentMoment: Instant = Clock.System.now()
    val localDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())

    return "${localDateTime.date} ${
        localDateTime.hour.toString().padStart(2, '0')
    }:${localDateTime.minute.toString().padStart(2, '0')}:${
        localDateTime.second.toString().padStart(2, '0')
    }"
}