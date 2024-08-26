package features.cashier_role.sales.domain

import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentApiModel(
    val message: String,
    val data: DataPayment,
    val code: String
)

@Serializable
data class DataPayment(
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

@Serializable
data class DetailPayment(
    val kode_detil_jual: String,
    val nama_barang: String,
    val jenis: String,
    val qty_jual: String,
    val harga_item: String,
    val subtotal: String,
    val diskon: String
)