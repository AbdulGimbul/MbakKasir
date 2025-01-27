package dev.mbakasir.com.features.cashier_role.sales.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePaymentApiModel(
    val message: String,
    val data: DataPayment,
    val code: String
) : dev.mbakasir.com.utils.JavaSerializable

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
) : dev.mbakasir.com.utils.JavaSerializable

@Serializable
data class DetailPayment(
    @SerialName("kode_detil_jual")
    val kodeDetilJual: String,
    @SerialName("nama_barang")
    val namaBarang: String,
    val jenis: String,
    @SerialName("qty_jual")
    val qtyJual: String,
    @SerialName("harga_item")
    val hargaItem: String,
    val subtotal: String,
    val diskon: String
) : dev.mbakasir.com.utils.JavaSerializable