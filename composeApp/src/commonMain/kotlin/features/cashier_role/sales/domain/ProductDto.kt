package features.cashier_role.sales.domain

import features.cashier_role.sales.data.ProductTransEntity
import kotlinx.serialization.Serializable

@Serializable
data class ProductTransSerializable(
    val idbarang: String,
    val kodebarang: String,
    val barcode: String,
    val namaBarang: String,
    val idKaryawan: String,
    val jenis: String,
    val qtyJual: Int,
    val hargaItem: Int,
    val diskon: Int,
    val subtotal: Int
)

fun ProductTransEntity.toSerializable(): ProductTransSerializable {
    return ProductTransSerializable(
        idbarang = this.idBarang,
        kodebarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        idKaryawan = this.idKaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyJual,
        hargaItem = this.hargaItem,
        diskon = this.diskon,
        subtotal = this.qtyJual * this.hargaItem - this.diskon
    )
}

fun ProductTransSerializable.toDetailPayload(): DetailPayload {
    return DetailPayload(
        idBarang = this.idbarang,
        idKaryawan = this.idKaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyJual.toString(),
        hargaItem = this.hargaItem.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon
    )
}

fun ProductTransSerializable.toDetailPayment(): DetailPayment {
    return DetailPayment(
        namaBarang = this.namaBarang,
        kodeDetilJual = this.idbarang,
        jenis = this.jenis,
        qtyJual = this.qtyJual.toString(),
        hargaItem = this.hargaItem.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon.toString()
    )
}