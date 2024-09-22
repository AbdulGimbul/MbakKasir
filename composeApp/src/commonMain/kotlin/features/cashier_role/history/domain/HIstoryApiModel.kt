package features.cashier_role.history.domain

import kotlinx.serialization.Serializable

@Serializable
data class HistoryApiModel(
    val data: List<PenjualanData>,
    val pagination: Pagination,
    val message: String,
    val code: String
)

@Serializable
data class PenjualanData(
    val invoice: String,
    val customer: String,
    val kasir: String,
    val method: String,
    val bayar: String,
    val kembali: String,
    val ppn: String,
    val device: String,
    val tanggal: String
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