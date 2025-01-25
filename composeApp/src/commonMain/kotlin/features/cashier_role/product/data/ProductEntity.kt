package features.cashier_role.product.data

import androidx.room.Entity
import androidx.room.PrimaryKey

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