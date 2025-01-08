package storage

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import features.cashier_role.home.domain.ProductMbakKasirDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<ProductMbakKasirDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(ProductMbakKasirDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}