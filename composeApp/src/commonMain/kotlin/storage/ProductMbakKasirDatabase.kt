package storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import features.cashier_role.product.data.ProductDao
import features.cashier_role.product.data.ProductEntity
import features.cashier_role.sales.data.ProductTransDraftDao
import features.cashier_role.sales.data.ProductTransDraftEntity
import features.cashier_role.sales.data.ProductTransEntity
import utils.DateTimeConverter

@Database(
    entities = [ProductEntity::class, ProductTransEntity::class, ProductTransDraftEntity::class],
    version = 1
)
@TypeConverters(DateTimeConverter::class)
@ConstructedBy(ProductDatabaseConstructor::class)
abstract class ProductMbakKasirDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val productTransDraftDao: ProductTransDraftDao

    companion object {
        const val DB_NAME = "mbakasir.db"
    }
}