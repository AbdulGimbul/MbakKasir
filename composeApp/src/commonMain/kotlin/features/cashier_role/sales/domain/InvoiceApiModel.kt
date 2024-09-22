package features.cashier_role.sales.domain

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceApiModel(
    val invoice: String,
    val data: Data,
    val message: String,
    val code: String
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