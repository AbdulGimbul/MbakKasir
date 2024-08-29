package features.cashier_role.sales.domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class ProductTrans : RealmObject {
    @PrimaryKey
    var id_barang: String = ""
    var kode_barang: String = ""
    var barcode: String = ""
    var nama_barang: String = ""
    var id_karyawan: String = ""
    var jenis: String = "Produk"
    var qty_jual: Int = 1
    var harga_item: Int = 0
    var diskon: Int = 0
    val subtotal: Int
        get() = qty_jual * harga_item - diskon
}

fun ProductTrans.toDetailPayload(): DetailPayload {
    return DetailPayload(
        id_barang = this.id_barang,
        id_karyawan = this.id_karyawan,
        jenis = this.jenis,
        qty_jual = this.qty_jual.toString(),
        harga_item = this.harga_item.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon
    )
}