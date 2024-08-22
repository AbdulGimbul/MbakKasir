package features.cashier_role.home.domain

import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
data class ProductApiModel(
    val barangs: List<Barang>
)

@Serializable
data class Barang(
    var id_barang: String?,
    var kode_barang: String?,
    var barcode: String?,
    var nama_barang: String?,
    var satuan: String?,
    var harga_jual: String?,
    var stok: String?
)

fun Barang.toProduct(): Product {
    return Product().apply {
        id_barang = ObjectId(this@toProduct.id_barang.toString())
        kode_barang = this@toProduct.kode_barang.toString()
        barcode = this@toProduct.barcode.toString()
        nama_barang = this@toProduct.nama_barang.toString()
        satuan = this@toProduct.satuan.toString()
        harga_jual = this@toProduct.harga_jual.toString()
        stok = this@toProduct.stok.toString()
    }
}