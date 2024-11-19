package features.cashier_role.sales.domain

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.Serializable

class ProductTransDraft : RealmObject {
    @PrimaryKey
    var draftId: String = ""
    var datetime: String = ""
    var kasir: String = ""
    var detail: RealmList<ProductTrans> = realmListOf()
    val totalTagihan: Int
        get() = detail.sumOf { it.subtotal }
    var isPrinted: Boolean = false
}

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

@Serializable
data class ProductTransSerializable(
    val id_barang: String,
    val kode_barang: String,
    val barcode: String,
    val nama_barang: String,
    val id_karyawan: String,
    val jenis: String,
    val qty_jual: Int,
    val harga_item: Int,
    val diskon: Int,
    val subtotal: Int
)

fun ProductTrans.toSerializable(): ProductTransSerializable {
    return ProductTransSerializable(
        id_barang = this.id_barang,
        kode_barang = this.kode_barang,
        barcode = this.barcode,
        nama_barang = this.nama_barang,
        id_karyawan = this.id_karyawan,
        jenis = this.jenis,
        qty_jual = this.qty_jual,
        harga_item = this.harga_item,
        diskon = this.diskon,
        subtotal = this.subtotal
    )
}

fun ProductTransSerializable.toDetailPayload(): DetailPayload {
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

fun ProductTransSerializable.toDetailPayment(): DetailPayment {
    return DetailPayment(
        namaBarang = this.nama_barang,
        kodeDetilJual = this.id_barang,
        jenis = this.jenis,
        qtyJual = this.qty_jual.toString(),
        hargaItem = this.harga_item.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon.toString()
    )
}

fun ProductTrans.copyWithNewId(newId: String): ProductTrans {
    return ProductTrans().apply {
        id_barang = newId
        kode_barang = this@copyWithNewId.kode_barang
        barcode = this@copyWithNewId.barcode
        nama_barang = this@copyWithNewId.nama_barang
        id_karyawan = this@copyWithNewId.id_karyawan
        jenis = this@copyWithNewId.jenis
        qty_jual = this@copyWithNewId.qty_jual
        harga_item = this@copyWithNewId.harga_item
        diskon = this@copyWithNewId.diskon
    }
}