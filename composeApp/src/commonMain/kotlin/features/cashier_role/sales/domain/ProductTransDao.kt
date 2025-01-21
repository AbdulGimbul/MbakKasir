package features.cashier_role.sales.domain

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import utils.currentTimeCustom

//@Dao
//interface ProductTransDao {

//}
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addTransaction(productTrans: ProductTransEntity)
//
//    @Query("SELECT * FROM product_trans WHERE draftId = :draftId ORDER BY idBarang DESC")
//    fun getProductsFromDraft(draftId: String): Flow<List<ProductTransEntity>>
//
//    @Query("DELETE FROM product_trans WHERE idBarang = :productId AND draftId = :draftId")
//    suspend fun deleteProductFromDraft(draftId: String, productId: String)
//
//    @Transaction
//    suspend fun insertProductTrans(
//        draftId: String,
//        cashierName: String,
//        productTransEntity: ProductTransEntity
//    ) {
//        val existingDraft = getDraftById(draftId)
//        if (existingDraft == null) {
//            insertDraft(
//                ProductTransDraftEntity(
//                    draftId = draftId,
//                    dateTime = currentTimeCustom(),
//                    cashier = cashierName
//                )
//            )
//        }
//
//        val correctedEntity = productTransEntity.copy(
//            subtotal = productTransEntity.qtyjual * productTransEntity.hargaitem
//        )
//
//        insertProductTrans(correctedEntity)
//    }
//
//    @Query("SELECT * FROM product_trans_drafts WHERE draftId = :draftId LIMIT 1")
//    suspend fun getDraftById(draftId: String): ProductTransDraftEntity?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertDraft(draft: ProductTransDraftEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProductTrans(transactions: List<ProductTransEntity>)
//}