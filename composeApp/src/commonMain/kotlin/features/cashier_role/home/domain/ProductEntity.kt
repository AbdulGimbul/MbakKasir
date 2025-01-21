package features.cashier_role.home.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import features.cashier_role.sales.domain.ProductTransEntity

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey var idBarang: String,
    var kodeBarang: String = "",
    var barcode: String = "",
    var namaBarang: String = "",
    var satuan: String = "",
    var hargaJual: String = "",
    var stok: String = ""
)

fun ProductEntity.toProductTrans(draftId: String): ProductTransEntity {
    return ProductTransEntity(
        draftId = draftId,
        idBarang = this.idBarang,
        kodeBarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        qtyJual = 1,
        hargaItem = this.hargaJual.toIntOrNull() ?: 0
    )
}