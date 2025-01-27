package dev.mbakasir.com.utils

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class DateTimeConverter {

    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): Long {
        // Convert LocalDateTime to epoch milliseconds
        return value.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    @TypeConverter
    fun toLocalDateTime(value: Long): LocalDateTime {
        // Convert epoch milliseconds back to LocalDateTime
        return Instant.fromEpochMilliseconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
    }
}