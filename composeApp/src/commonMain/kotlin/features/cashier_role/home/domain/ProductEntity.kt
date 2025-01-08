package features.cashier_role.home.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import features.cashier_role.sales.domain.ProductTransEntity

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey var idBarang: String, // Primary key field
    var kodeBarang: String = "",
    var barcode: String = "",
    var namaBarang: String = "",
    var satuan: String = "",
    var hargaJual: String = "",
    var stok: String = ""
)

fun ProductEntity.toProductTrans(): ProductTransEntity {
    return ProductTransEntity(
        idBarang = this.idBarang,
        kodeBarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        hargaitem = this.hargaJual.toIntOrNull() ?: 0 // Handle potential conversion errors
    )
}