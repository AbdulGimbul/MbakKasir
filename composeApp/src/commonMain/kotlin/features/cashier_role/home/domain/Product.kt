package features.cashier_role.home.domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Product : RealmObject {
    @PrimaryKey
    var id_barang: String = ""
    var kode_barang: String = ""
    var barcode: String = ""
    var nama_barang: String = ""
    var satuan: String = ""
    var harga_jual: String = ""
    var stok: String = ""
}

fun Product.toBarang(): Barang {
    return Barang(
        id_barang = this.id_barang,
        kode_barang = this.kode_barang,
        barcode = this.barcode,
        nama_barang = this.nama_barang,
        satuan = this.satuan,
        harga_jual = this.harga_jual,
        stok = this.stok
    )
}