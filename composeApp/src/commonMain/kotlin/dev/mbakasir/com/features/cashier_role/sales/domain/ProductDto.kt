package dev.mbakasir.com.features.cashier_role.sales.domain

import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity
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

fun ProductTransEntity.toSerializable(customerType: String = ""): ProductTransSerializable {
    if (customerType.isBlank()) {
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

    val specialPrice =
        when (customerType) {
            "Pelanggan" ->
                if (this.hargaPelanggan > 0) this.hargaPelanggan else this.hargaItem

            "Toko" -> if (this.hargaToko > 0) this.hargaToko else this.hargaItem
            "Sales" -> if (this.hargaSales > 0) this.hargaSales else this.hargaItem
            else -> this.hargaItem
        }
    val calculatedDiscount = (this.hargaItem - specialPrice) * this.qtyJual

    return ProductTransSerializable(
        idbarang = this.idBarang,
        kodebarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        idKaryawan = this.idKaryawan,
        jenis = this.jenis,
        qtyJual = this.qtyJual,
        hargaItem = this.hargaItem,
        diskon = calculatedDiscount,
        subtotal = this.qtyJual * this.hargaItem - calculatedDiscount
    )
}

fun ProductTransSerializable.toDetailPayload(): DetailPayload {
    val itemSubtotal = this.qtyJual * this.hargaItem
    val itemTotal = itemSubtotal - this.diskon

    return DetailPayload(
        idBarang = this.idbarang.toIntOrNull() ?: 0,
        idKaryawan = this.idKaryawan.toIntOrNull(),
        jenis = this.jenis,
        qtyJual = this.qtyJual,
        hargaItem = this.hargaItem,
        itemSubtotal = itemSubtotal,
        discountType = "fixed",
        discountValue = this.diskon,
        itemTotal = itemTotal,
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
