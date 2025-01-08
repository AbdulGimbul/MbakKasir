package storage

import androidx.room.Room
import androidx.room.RoomDatabase
import features.cashier_role.home.domain.ProductMbakKasirDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ProductMbakKasirDatabase> {
        val dbFile = documentDirectory() + "/${ProductMbakKasirDatabase.DB_NAME}"
        return Room.databaseBuilder<ProductMbakKasirDatabase>(
            name = dbFile
        )
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}