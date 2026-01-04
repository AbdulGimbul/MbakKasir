package dev.mbakasir.com.features.cashier_role.sales.domain

import dev.mbakasir.com.utils.JavaSerializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentApiModel(
        val message: String,
        val data: DataPayment? = null,
        val code: String,
        @SerialName("total_harga") val totalHarga: Int? = null,
        @SerialName("total_diskon") val totalDiskon: Int? = null,
        @SerialName("ppn") val ppn: Int? = null,
        @SerialName("total_tagihan") val totalTagihan: Int? = null
) : JavaSerializable

@Serializable
data class DataPayment(
        val invoice: String? = null,
        val customer: String? = null,
        val kasir: String? = null,
        val method: String? = null,
        val bayar: String? = null,
        val kembali: String? = null,
        val ppn: String? = null,
        val device: String? = null,
        val tanggal: String? = null,
        val detil: List<DetailPayment> = emptyList(),
        @SerialName("no_invoice") val noInvoice: String? = null
) : JavaSerializable

@Serializable
data class DetailPayment(
        @SerialName("kode_detil_jual") val kodeDetilJual: String,
        @SerialName("nama_barang") val namaBarang: String,
        val jenis: String,
        @SerialName("qty_jual") val qtyJual: String,
        @SerialName("harga_item") val hargaItem: String,
        val subtotal: String,
        val diskon: String
) : JavaSerializable
