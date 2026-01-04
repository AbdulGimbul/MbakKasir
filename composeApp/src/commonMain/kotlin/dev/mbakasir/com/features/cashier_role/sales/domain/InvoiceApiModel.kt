package dev.mbakasir.com.features.cashier_role.sales.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InvoiceApiModel(
        val invoice: String,
        val data: Data,
        val message: String,
        val code: String,
        @SerialName("total_harga") val totalHarga: Int,
        @SerialName("total_diskon") val totalDiskon: Int,
        @SerialName("ppn") val ppn: Int,
        @SerialName("total_tagihan") val totalTagihan: Int
)

@Serializable
data class Data(
        val invoice: String,
        val customer: String,
        val kasir: String,
        val method: String,
        val bayar: String,
        val kembali: String,
        val ppn: String,
        val device: String,
        val tanggal: String,
        val detil: List<DetailPayment>
)
