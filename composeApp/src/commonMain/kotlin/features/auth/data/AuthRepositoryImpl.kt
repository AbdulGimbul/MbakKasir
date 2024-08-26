package features.auth.data

import features.auth.domain.LoginApiModel
import features.auth.domain.SalesHistoryApiModel
import network.NetworkError
import network.NetworkResult
import network.RequestHandler

class AuthRepositoryImpl(
    private val requestHandler: RequestHandler
) : AuthRepository {
    override suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkError> {
        return requestHandler.post(
            urlPathSegments = listOf("api", "login"),
            body = request
        )
    }

    override suspend fun isTokenValid(
        starDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<SalesHistoryApiModel, NetworkError> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "get"),
            queryParams = mapOf(
                "startDate" to starDate,
                "endDate" to endDate,
                "page" to page,
                "perPage" to perPage
            )
        )
    }
}