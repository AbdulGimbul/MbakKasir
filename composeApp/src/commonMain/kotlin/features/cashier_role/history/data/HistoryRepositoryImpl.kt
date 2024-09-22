package features.cashier_role.history.data

import features.cashier_role.history.domain.HistoryApiModel
import network.NetworkError
import network.NetworkResult
import network.RequestHandler

class HistoryRepositoryImpl(
    private val requestHandler: RequestHandler
) : HistoryRepository {
    override suspend fun getHistory(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<HistoryApiModel, NetworkError> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "get"),
            queryParams = mapOf(
                "startDate" to startDate,
                "endDate" to endDate,
                "page" to page,
                "perPage" to perPage
            )
        )
    }
}