package features.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class SalesHistoryApiModel(
    var data: ArrayList<Data> = arrayListOf(),
    var pagination: Pagination? = Pagination(),
    var message: String? = null,
    var code: String? = null
)

@Serializable
data class Data(
    var invoice: String? = null,
    var customer: String? = null,
    var kasir: String? = null,
    var method: String? = null,
    var bayar: String? = null,
    var kembali: String? = null,
    var ppn: String? = null,
    var device: String? = null,
    var tanggal: String? = null

)

@Serializable
data class Pagination(
    var currentPage: Int? = null,
    var totalPages: Int? = null,
    var perPage: String? = null,
    var totalItems: Int? = null,
    var hasNextPage: Boolean? = null,
    var nextPageUrl: String? = null,
    var previousPageUrl: String? = null

)