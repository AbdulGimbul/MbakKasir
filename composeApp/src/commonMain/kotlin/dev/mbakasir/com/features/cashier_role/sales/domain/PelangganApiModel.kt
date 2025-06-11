package dev.mbakasir.com.features.cashier_role.sales.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PelangganApiModel(
    @SerialName("pelanggans")
    val customers: List<Customer>
)

@Serializable
data class Customer(
    val kode: String,
    val nama: String,
    val telp: String,
    val email: String,
    val alamat: String
)
