package dev.mbakasir.com.features.cashier_role.sales.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "product_trans_drafts")
data class ProductTransDraftEntity(
    @PrimaryKey val draftId: String = "",
    val dateTime: String = "",
    val cashier: String = "",
    var customer: String = "",
    var isPrinted: Boolean = false,
    var amountPaid: Int = 0,
    var paymentMethod: String = "",
    var dueDate: String = ""
)

@Entity(tableName = "product_trans")
data class ProductTransEntity(
    @PrimaryKey val idBarang: String = "",
    val draftId: String = "",
    val kodeBarang: String = "",
    val barcode: String = "",
    val namaBarang: String = "",
    val idKaryawan: String = "",
    val jenis: String = "Produk",
    var qtyJual: Int = 1,
    val hargaItem: Int = 0,
    val diskon: Int = 0,
    val subtotal: Int = 0
)

data class ProductDraftWithItems(
    @Embedded val draft: ProductTransDraftEntity,
    @Relation(
        parentColumn = "draftId",
        entityColumn = "draftId"
    )
    val items: List<ProductTransEntity>
) {
    val totalAmount: Int
        get() = items.sumOf { it.qtyJual * it.hargaItem - it.diskon }
    val change: Int
        get() = draft.amountPaid - totalAmount
}