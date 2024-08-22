package features.cashier_role.home.domain

import kotlinx.serialization.Serializable

@Serializable
data class ProductApiModel(
    val barangs: List<Barang>
)

@Serializable
data class Barang(
    var id_barang: String,
    var kode_barang: String,
    var barcode: String,
    var nama_barang: String,
    var satuan: String,
    var harga_jual: String,
    var stok: String
)