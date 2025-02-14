package dev.mbakasir.com.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
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

fun formatDateTime(localDateTime: LocalDateTime): String {
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val month = when (localDateTime.month) {
        Month.JANUARY -> "Jan"
        Month.FEBRUARY -> "Feb"
        Month.MARCH -> "Mar"
        Month.APRIL -> "Apr"
        Month.MAY -> "Mei"
        Month.JUNE -> "Jun"
        Month.JULY -> "Jul"
        Month.AUGUST -> "Agu"
        Month.SEPTEMBER -> "Sep"
        Month.OCTOBER -> "Okt"
        Month.NOVEMBER -> "Nov"
        Month.DECEMBER -> "Des"
        else -> {
            ""
        }
    }
    val year = localDateTime.year
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    val second = localDateTime.second.toString().padStart(2, '0')

    return "$day $month $year $hour:$minute:$second"

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

fun formatDateRange(startTimestamp: Long, endTimestamp: Long): String {
    val startInstant = Instant.fromEpochMilliseconds(startTimestamp)
    val endInstant = Instant.fromEpochMilliseconds(endTimestamp)

    val startLocalDateTime = startInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val endLocalDateTime = endInstant.toLocalDateTime(TimeZone.currentSystemDefault())

    val startFormatted = "${startLocalDateTime.date.dayOfMonth.toString().padStart(2, '0')} " +
            "${startLocalDateTime.date.month.name.take(3)} " +
            "${startLocalDateTime.date.year}"

    val endFormatted = "${endLocalDateTime.date.dayOfMonth.toString().padStart(2, '0')} " +
            "${endLocalDateTime.date.month.name.take(3)} " +
            "${endLocalDateTime.date.year}"

    return "$startFormatted - $endFormatted"
}

fun formatDateForApi(timestamp: Long): String {
    val localDateTime = Instant.fromEpochMilliseconds(timestamp)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return "${localDateTime.date.dayOfMonth.toString().padStart(2, '0')}-" +
            "${localDateTime.date.monthNumber.toString().padStart(2, '0')}-" +
            "${localDateTime.date.year}"
}

fun getTodayDate(): String {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    return formatDateForApi(
        today.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )
}

fun getLastWeekDate(): String {
    val lastWeek = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        .minus(DatePeriod(days = 7))
    return formatDateForApi(
        lastWeek.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )
}