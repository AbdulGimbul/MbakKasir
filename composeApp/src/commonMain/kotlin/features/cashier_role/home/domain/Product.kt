package features.cashier_role.home.domain

import features.cashier_role.sales.domain.ProductTrans
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

fun Product.toProductTrans(): ProductTrans {
    return ProductTrans(
        id_barang = this.id_barang,
        harga_item = this.harga_jual.toInt(),
        kode_barang = this.kode_barang,
        barcode = this.barcode,
        nama_barang = this.nama_barang
    )
}