package storage

import androidx.room.RoomDatabase
import features.cashier_role.home.domain.ProductMbakKasirDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<ProductMbakKasirDatabase>
}