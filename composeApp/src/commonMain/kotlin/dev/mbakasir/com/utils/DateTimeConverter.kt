package dev.mbakasir.com.utils

import androidx.room.TypeConverter
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class DateTimeConverter {

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): Long {
        // Convert LocalDateTime to epoch milliseconds
        return value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime {
        // Convert epoch milliseconds back to LocalDateTime
        return Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
    }
}