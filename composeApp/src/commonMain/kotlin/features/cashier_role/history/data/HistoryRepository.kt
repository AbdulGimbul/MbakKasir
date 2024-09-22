package features.cashier_role.history.data

import features.cashier_role.history.domain.HistoryApiModel
import network.NetworkError
import network.NetworkResult

interface HistoryRepository {
    suspend fun getHistory(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<HistoryApiModel, NetworkError>
}