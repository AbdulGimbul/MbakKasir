package dev.mbakasir.com.features.cashier_role.sales.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentRequest(
        val kembali: Int,
        val bayar: Int,
        val metode: String,
        val keterangan: String,
        val kasir: Int,
        val cus: String,
        @SerialName("ppn_percentage") val ppnPercentage: Int,
        @SerialName("nominal_ppn") val nominalPpn: Int,
        val tempo: String,
        @SerialName("no_invoice") val noInvoice: String,
        val detil: List<DetailPayload>
)

@Serializable
data class DetailPayload(
        @SerialName("id_barang") val idBarang: Int,
        @SerialName("id_karyawan") val idKaryawan: Int?,
        val jenis: String,
        @SerialName("qty_jual") val qtyJual: Int,
        @SerialName("harga_item") val hargaItem: Int,
        @SerialName("item_subtotal") val itemSubtotal: Int,
        @SerialName("discount_type") val discountType: String,
        @SerialName("discount_value") val discountValue: Int,
        @SerialName("item_total") val itemTotal: Int,
        val diskon: Int
)
