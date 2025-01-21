package features.cashier_role.home.domain

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import features.cashier_role.sales.domain.ProductTransDraftDao
import features.cashier_role.sales.domain.ProductTransDraftEntity
import features.cashier_role.sales.domain.ProductTransEntity

@Database(
    entities = [ProductEntity::class, ProductTransEntity::class, ProductTransDraftEntity::class],
    version = 1
)
@ConstructedBy(ProductDatabaseConstructor::class)
abstract class ProductMbakKasirDatabase: RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val productTransDraftDao: ProductTransDraftDao

    companion object {
        const val DB_NAME = "mbakasir.db"
    }
}