package features.cashier_role.sales.domain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.serialization.Serializable

@Entity(tableName = "product_trans_drafts")
data class ProductTransDraftEntity(
    @PrimaryKey val draftId: String = "",
    val dateTime: String = "",
    val cashier: String = "",
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

@Serializable
data class ProductTransSerializable(
    val idbarang: String,
    val kodebarang: String,
    val barcode: String,
    val namaBarang: String,
    val idKaryawan: String,
    val jenis: String,
    val qtyJual: Int,
    val hargaItem: Int,
    val diskon: Int,
    val subtotal: Int
)

fun ProductTransEntity.toSerializable(): ProductTransSerializable {
    return ProductTransSerializable(
        idbarang = this.idBarang,
        kodebarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        idKaryawan = this.idKaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyJual,
        hargaItem = this.hargaItem,
        diskon = this.diskon,
        subtotal = this.subtotal
    )
}

fun ProductTransSerializable.toDetailPayload(): DetailPayload {
    return DetailPayload(
        idBarang = this.idbarang,
        idKaryawan = this.idKaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyJual.toString(),
        hargaItem = this.hargaItem.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon
    )
}

fun ProductTransSerializable.toDetailPayment(): DetailPayment {
    return DetailPayment(
        namaBarang = this.namaBarang,
        kodeDetilJual = this.idbarang,
        jenis = this.jenis,
        qtyJual = this.qtyJual.toString(),
        hargaItem = this.hargaItem.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon.toString()
    )
}

fun ProductTransEntity.copyWithNewId(newId: String): ProductTransEntity {
    return this.copy(idBarang = newId)
}