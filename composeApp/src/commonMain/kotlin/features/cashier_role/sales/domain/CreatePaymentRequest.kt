package features.cashier_role.sales.domain

import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentRequest(
    val kembali: String,
    val bayar: String,
    val metode: String,
    val kasir: String,
    val cus: String,
    val nominal_ppn: String,
    val tempo: String,
    val detil: List<DetailPayload>
)

@Serializable
data class DetailPayload(
    val id_barang: String,
    val id_karyawan: String,
    val jenis: String,
    val qty_jual: String,
    val harga_item: String,
    val subtotal: String,
    val diskon: Int
)