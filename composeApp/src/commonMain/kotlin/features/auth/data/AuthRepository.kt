package features.auth.data

import features.auth.domain.LoginApiModel
import features.auth.domain.LoginRequest
import features.auth.domain.SalesHistoryApiModel
import features.auth.domain.UserData
import network.NetworkError
import network.NetworkResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkError>
    suspend fun isTokenValid(
        starDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<SalesHistoryApiModel, NetworkError>

    suspend fun userInfo(): UserData
}