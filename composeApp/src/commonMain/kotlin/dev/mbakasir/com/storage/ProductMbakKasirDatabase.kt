package dev.mbakasir.com.storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.mbakasir.com.features.cashier_role.product.data.ProductDao
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.data.CustomerDao
import dev.mbakasir.com.features.cashier_role.sales.data.CustomerEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransDraftDao
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransDraftEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity
import dev.mbakasir.com.utils.DateTimeConverter

@Database(
    entities =
        [
            ProductEntity::class,
            ProductTransEntity::class,
            ProductTransDraftEntity::class,
            CustomerEntity::class],
    version = 4
)
@TypeConverters(DateTimeConverter::class)
@ConstructedBy(ProductDatabaseConstructor::class)
abstract class ProductMbakKasirDatabase : RoomDatabase() {
    abstract val productDao: ProductDao
    abstract val productTransDraftDao: ProductTransDraftDao
    abstract val customerDao: CustomerDao

    companion object {
        const val DB_NAME = "mbakasir.db"
    }
}

expect object ProductDatabaseConstructor : RoomDatabaseConstructor<ProductMbakKasirDatabase> {
    override fun initialize(): ProductMbakKasirDatabase
}
