package dev.mbakasir.com.features.admin_role.stock_opname.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateStockOpnameReq(
    @SerialName("id_barang")
    val idBarang: String,
    val jenis: String,
    val harga: String,
    val jumlah: String,
    val keterangan: String,
    @SerialName("create_by")
    val createdBy: String
)