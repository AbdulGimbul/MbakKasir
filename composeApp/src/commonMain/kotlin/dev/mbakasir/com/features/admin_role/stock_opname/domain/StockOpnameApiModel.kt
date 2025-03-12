package dev.mbakasir.com.features.admin_role.stock_opname.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockOpnameApiModel(
    val data: List<StockOpnameData>,
    val pagination: Pagination,
    val message: String,
    val code: String
)

@Serializable
data class StockOpnameData(
    @SerialName("nama_barang")
    val namaBarang: String,
    @SerialName("jumlah_update")
    val jumlahUpdate: String,
    @SerialName("jumlah_sebelum")
    val jumlahSebelum: String,
    val nilai: String,
    @SerialName("jenis_transaksi")
    val jenisTransaksi: String,
    val keterangan: String,
    val tanggal: String,
    @SerialName("create_by")
    val createdBy: String
)

@Serializable
data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val perPage: String,
    val totalItems: Int,
    val hasNextPage: Boolean,
    val nextPageUrl: String? = null,
    val previousPageUrl: String? = null
)