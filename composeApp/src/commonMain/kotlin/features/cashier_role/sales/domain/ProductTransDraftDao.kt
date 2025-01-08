package features.cashier_role.sales.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import utils.currentTimeCustom
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Dao
interface ProductTransDraftDao {
    @Transaction
    @Query("SELECT * FROM product_trans_drafts WHERE draftId = :draftId")
    suspend fun getDraftWithItems(draftId: String): ProductTransDraftWithItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: ProductTransDraftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductTrans(items: List<ProductTransEntity>)

    @Update
    suspend fun updateDraft(draft: ProductTransDraftEntity)

    @Delete
    suspend fun deleteProductTrans(item: ProductTransEntity)

    @Query("DELETE FROM product_trans_drafts WHERE draftId = :draftId")
    suspend fun deleteDraftById(draftId: String)

    @Query("SELECT * FROM product_trans_drafts ORDER BY draftId DESC")
    fun getDrafts(): Flow<List<ProductTransDraftEntity>>

    @OptIn(ExperimentalUuidApi::class)
    @Transaction
    suspend fun addProductTransToDraft(
        draftId: String,
        cashierName: String,
        productTransEntity: ProductTransEntity
    ) {
        val existingDraft = getDraftWithItems(draftId)

        val clonedProduct = productTransEntity.copyWithNewId(Uuid.random().toString())

        if (existingDraft != null) {
            val updatedItems = existingDraft.items.toMutableList()
            updatedItems.add(clonedProduct)
            insertProductTrans(updatedItems)
        } else {
            val newDraft = ProductTransDraftEntity(
                draftId = draftId,
                dateTime = currentTimeCustom(),
                cashier = cashierName
            )
            insertDraft(newDraft)
            insertProductTrans(listOf(clonedProduct))
        }
    }

    @Transaction
    suspend fun updateProductTransInDraft(
        draftId: String,
        productId: String? = null,
        qty: Int? = null,
        amountPaid: Int? = null,
        paymentMethod: String? = null,
        dueDate: String? = null,
        isPrinted: Boolean? = null,
    ) {
        val draftWithItems = getDraftWithItems(draftId)

        draftWithItems?.let { existingDraftWithItems ->
            val existingDraft = existingDraftWithItems.draft
            val items = existingDraftWithItems.items.toMutableList()

            productId?.let { id ->
                val product = items.firstOrNull { it.idBarang == id }
                product?.let {
                    if (qty != null) {
                        it.qtyjual = qty
                        if (qty < 1) {
                            items.remove(it)
                            deleteProductTrans(it)
                        } else {
                            insertProductTrans(listOf(it))
                        }
                    }
                }
            }

            if (items.isEmpty()) {
                deleteDraftById(existingDraft.draftId)
            } else {
                isPrinted?.let { existingDraft.isPrinted = it }
                amountPaid?.let { existingDraft.amountPaid = it }
                paymentMethod?.let { existingDraft.paymentMethod = it }
                dueDate?.let { existingDraft.dueDate = it }

                updateDraft(existingDraft)
                insertProductTrans(items) // Update remaining items
            }
        }
    }
}

data class ProductTransDraftWithItems(
    @Embedded val draft: ProductTransDraftEntity,
    @Relation(
        parentColumn = "draftId",
        entityColumn = "draftId"
    )
    val items: List<ProductTransEntity>
)