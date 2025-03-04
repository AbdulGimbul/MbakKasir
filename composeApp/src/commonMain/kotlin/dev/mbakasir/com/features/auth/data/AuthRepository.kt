package dev.mbakasir.com.features.auth.data

import dev.mbakasir.com.features.auth.domain.LoginApiModel
import dev.mbakasir.com.features.auth.domain.LoginRequest
import dev.mbakasir.com.features.auth.domain.LogoutApiModel
import dev.mbakasir.com.features.auth.domain.SalesHistoryApiModel
import dev.mbakasir.com.features.auth.domain.UserData
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkException>
    suspend fun isTokenValid(
        starDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<SalesHistoryApiModel, NetworkException>

    suspend fun userInfo(): UserData
    suspend fun logout(): NetworkResult<LogoutApiModel, NetworkException>
}