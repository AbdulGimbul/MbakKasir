package dev.mbakasir.com.storage

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ProductDatabaseConstructor : RoomDatabaseConstructor<ProductMbakKasirDatabase> {
    override fun initialize(): ProductMbakKasirDatabase
}