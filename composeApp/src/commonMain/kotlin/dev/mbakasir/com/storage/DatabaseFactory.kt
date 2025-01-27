package dev.mbakasir.com.storage

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<ProductMbakKasirDatabase>
}