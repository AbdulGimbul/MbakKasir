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