package dev.mbakasir.com.features.cashier_role.sales.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductTransDraftDao {
    @Transaction
    @Query("SELECT * FROM product_trans_drafts WHERE username = :username ORDER BY draftId DESC")
    fun getAllDrafts(username: String): Flow<List<ProductDraftWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: ProductTransDraftEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductTrans(product: ProductTransEntity)

    @Transaction
    suspend fun addProductToDraft(
            draftId: String,
            cashierName: String,
            product: ProductTransEntity,
            username: String
    ) {
        val draft = getDraftById(draftId)
        if (draft != null) {

            insertProductTrans(product)
            updateDraft(
                    draftId = draft.draftId,
                    isPrinted = draft.isPrinted,
                    amountPaid = draft.amountPaid,
                    paymentMethod = draft.paymentMethod,
                    description = draft.description,
                    dueDate = draft.dueDate,
                    customer = draft.customer,
                    username = username
            )
        } else {
            val newDraft =
                    ProductTransDraftEntity(
                            draftId = draftId,
                            cashier = cashierName,
                            username = username
                    )
            insertDraft(newDraft)
            insertProductTrans(product)
        }
    }

    @Query("SELECT * FROM product_trans WHERE draftId = :draftId ORDER BY idBarang DESC")
    fun getProductsByDraftId(draftId: String): Flow<List<ProductTransEntity>>

    @Query("SELECT * FROM product_trans_drafts WHERE draftId = :draftId LIMIT 1")
    suspend fun getDraftById(draftId: String): ProductTransDraftEntity?

    @Query(
            "UPDATE product_trans_drafts SET isPrinted = :isPrinted, amountPaid = :amountPaid, paymentMethod = :paymentMethod, dueDate = :dueDate, description = :description, customer = :customer, username = :username WHERE draftId = :draftId"
    )
    suspend fun updateDraft(
            draftId: String,
            isPrinted: Boolean?,
            amountPaid: Int?,
            paymentMethod: String?,
            description: String?,
            dueDate: String?,
            customer: String?,
            username: String?
    )

    @Transaction
    suspend fun updateProductInDraft(
            draftId: String,
            productId: String?,
            qty: Int?,
            amountPaid: Int?,
            paymentMethod: String?,
            description: String?,
            dueDate: String?,
            isPrinted: Boolean?,
            customer: String?
    ) {
        val draftWithItems = getDraftWithItemsById(draftId)
        draftWithItems?.let { existingDraftWithItems ->
            val draft = existingDraftWithItems.draft
            val product = existingDraftWithItems.items.firstOrNull { it.idBarang == productId }

            if (product != null) {
                if (qty != null && qty < 1) {
                    if (productId != null) {
                        deleteProductFromDraft(draftId, productId)
                    }

                    val remainingItems =
                            existingDraftWithItems.items.filter { it.idBarang != productId }
                    if (remainingItems.isEmpty()) {
                        deleteDraft(draftId)
                    }
                    return
                } else if (qty != null) {
                    product.qtyJual = qty
                    updateProductTrans(product)
                }
            } else {
                println("Product not found in draft for productId: $productId")
            }

            amountPaid?.let { draft.amountPaid = it }
            paymentMethod?.let { draft.paymentMethod = it }
            dueDate?.let { draft.dueDate = it }
            isPrinted?.let { draft.isPrinted = it }
            customer?.let { draft.customer = it }

            updateDraft(
                    draftId = draft.draftId,
                    isPrinted = draft.isPrinted,
                    amountPaid = draft.amountPaid,
                    paymentMethod = draft.paymentMethod,
                    description = draft.description,
                    dueDate = draft.dueDate,
                    customer = draft.customer,
                    username = draft.username
            )
        }
                ?: println("Draft not found for draftId: $draftId")
    }

    @Update suspend fun updateProductTrans(product: ProductTransEntity)

    @Transaction
    @Query("SELECT * FROM product_trans_drafts WHERE draftId = :draftId")
    suspend fun getDraftWithItemsById(draftId: String): ProductDraftWithItems?

    @Query("DELETE FROM product_trans_drafts WHERE draftId = :draftId")
    suspend fun deleteDraftById(draftId: String)

    @Query("DELETE FROM product_trans WHERE draftId = :draftId AND idBarang = :productId")
    suspend fun deleteProductFromDraft(draftId: String, productId: String)

    @Query("DELETE FROM product_trans_drafts WHERE draftId = :draftId")
    suspend fun deleteDraft(draftId: String)

    @Query("UPDATE product_trans_drafts SET customer = :customer WHERE draftId = :draftId")
    suspend fun updateDraftCustomer(draftId: String, customer: String)

    @Query("DELETE FROM product_trans_drafts WHERE username = :username")
    suspend fun deleteDraftsForUser(username: String)
}
