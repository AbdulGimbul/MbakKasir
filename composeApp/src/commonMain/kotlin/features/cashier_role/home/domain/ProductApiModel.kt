package features.cashier_role.home.domain

import kotlinx.serialization.Serializable

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

fun Barang.toProduct(): ProductEntity {
    return ProductEntity(
        idBarang = this.id_barang.toString(),
        kodeBarang = this.kode_barang.toString(),
        barcode = this.barcode.toString(),
        namaBarang = this.nama_barang.toString(),
        satuan = this.satuan.toString(),
        hargaJual = this.harga_jual.toString(),
        stok = this.stok.toString()
    )
}