package features.cashier_role.home.domain

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Product : RealmObject {
    @PrimaryKey
    var id_barang: ObjectId = ObjectId()
    var kode_barang: String = ""
    var barcode: String = ""
    var nama_barang: String = ""
    var satuan: String = ""
    var harga_jual: String = ""
    var stok: String = ""
}

// Extension function to convert Product to Barang
fun Product.toBarang(): Barang {
    return Barang(
        id_barang = this.id_barang.toHexString(),
        kode_barang = this.kode_barang,
        barcode = this.barcode,
        nama_barang = this.nama_barang,
        satuan = this.satuan,
        harga_jual = this.harga_jual,
        stok = this.stok
    )
}