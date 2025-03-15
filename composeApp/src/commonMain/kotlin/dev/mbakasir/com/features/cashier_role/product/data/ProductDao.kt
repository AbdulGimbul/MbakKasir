package dev.mbakasir.com.features.cashier_role.product.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM products ORDER BY idBarang DESC")
    fun getProducts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM products WHERE idBarang = :id")
    suspend fun deleteProductById(id: String)

    @Query(
        """
        SELECT * FROM products 
        WHERE barcode LIKE '%' || :query || '%' 
           OR namaBarang LIKE '%' || :query || '%' 
           OR kodeBarang LIKE '%' || :query || '%' 
        ORDER BY barcode DESC
    """
    )
    fun searchProductsByBarcode(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    fun getProductByBarcode(barcode: String): Flow<ProductEntity?>

    @Query("SELECT COUNT(*) > 0 FROM products")
    fun isProductCacheAvailable(): Flow<Boolean>

    @Query("SELECT * FROM products ORDER BY stok DESC LIMIT :pageSize OFFSET :offset")
    fun getTopProductsByStok(pageSize: Int, offset: Int): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    fun calculateTotalProducts(): Flow<Int>

    @Query("UPDATE products SET stok = :newStok WHERE idBarang = :id")
    suspend fun updateStockById(id: String, newStok: String)
}