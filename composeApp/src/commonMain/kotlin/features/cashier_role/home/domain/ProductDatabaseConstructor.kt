package features.cashier_role.home.domain

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ProductDatabaseConstructor: RoomDatabaseConstructor<ProductMbakKasirDatabase> {
    override fun initialize(): ProductMbakKasirDatabase
}