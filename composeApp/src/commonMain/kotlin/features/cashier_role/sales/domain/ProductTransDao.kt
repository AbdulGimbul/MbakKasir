package features.cashier_role.sales.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductTransDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(productTrans: ProductTransEntity)

    @Query("SELECT * FROM product_trans WHERE draftId = :draftId ORDER BY idBarang DESC")
    fun getProductsFromDraft(draftId: String): Flow<List<ProductTransEntity>>

    @Query("DELETE FROM product_trans WHERE idBarang = :productId AND draftId = :draftId")
    suspend fun deleteProductFromDraft(draftId: String, productId: String)
}