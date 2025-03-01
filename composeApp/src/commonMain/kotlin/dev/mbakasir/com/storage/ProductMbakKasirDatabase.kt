package dev.mbakasir.com.storage

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import dev.mbakasir.com.features.cashier_role.product.data.ProductDao
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransDraftDao
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransDraftEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity
import dev.mbakasir.com.utils.DateTimeConverter

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

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object ProductDatabaseConstructor : RoomDatabaseConstructor<ProductMbakKasirDatabase> {
    override fun initialize(): ProductMbakKasirDatabase
}