package features.cashier_role.sales.domain

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "product_trans_drafts")
data class ProductTransDraftEntity(
    @PrimaryKey val draftId: String,
    val dateTime: String = "",
    val cashier: String = "",
    var isPrinted: Boolean = false,
    var amountPaid: Int = 0,
    var paymentMethod: String = "",
    var dueDate: String = "",
    var totalAmount: Int = 0
) {
    @Ignore
    val items: List<ProductTransEntity> = emptyList()

    val change: Int
        get() = amountPaid - totalAmount
}

@Entity(
    tableName = "product_trans",
    foreignKeys = [
        ForeignKey(
            entity = ProductTransDraftEntity::class,
            parentColumns = ["draftId"],
            childColumns = ["draftId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["draftId"])]
)
data class ProductTransEntity(
    @PrimaryKey val idBarang: String = "",
    val draftId: String = "",
    val kodeBarang: String = "",
    val barcode: String = "",
    val namaBarang: String = "",
    val idkaryawan: String = "",
    val jenis: String = "Produk",
    var qtyjual: Int = 1,
    val hargaitem: Int = 0,
    val diskon: Int = 0,
    val subtotal: Int = 0
) {
    fun calculateSubtotal(): Int = qtyjual * hargaitem - diskon
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
        idKaryawan = this.idkaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyjual,
        hargaItem = this.hargaitem,
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