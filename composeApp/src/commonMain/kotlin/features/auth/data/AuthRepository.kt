package features.auth.data

import features.auth.domain.LoginApiModel
import features.auth.domain.LoginRequest
import features.auth.domain.SalesHistoryApiModel
import features.auth.domain.UserData
import network.NetworkException
import network.NetworkResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkException>
    suspend fun isTokenValid(
        starDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<SalesHistoryApiModel, NetworkException>

    suspend fun userInfo(): UserData
    suspend fun logout()
}