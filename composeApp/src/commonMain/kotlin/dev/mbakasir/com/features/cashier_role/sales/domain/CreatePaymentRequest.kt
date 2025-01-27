package dev.mbakasir.com.features.cashier_role.sales.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentRequest(
    val kembali: String,
    val bayar: String,
    val metode: String,
    val kasir: String,
    val cus: String,
    @SerialName("nominal_ppn")
    val nominalPpn: String,
    val tempo: String,
    val detil: List<DetailPayload>
)

@Serializable
data class DetailPayload(
    @SerialName("id_barang")
    val idBarang: String,
    @SerialName("id_karyawan")
    val idKaryawan: String,
    val jenis: String,
    @SerialName("qty_jual")
    val qtyJual: String,
    @SerialName("harga_item")
    val hargaItem: String,
    val subtotal: String,
    val diskon: Int
)