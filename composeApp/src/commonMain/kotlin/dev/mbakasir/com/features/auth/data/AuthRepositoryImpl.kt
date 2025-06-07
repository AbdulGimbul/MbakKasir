package dev.mbakasir.com.features.auth.data

import dev.mbakasir.com.features.auth.domain.GetVersionApiModel
import dev.mbakasir.com.features.auth.domain.LoginApiModel
import dev.mbakasir.com.features.auth.domain.LoginRequest
import dev.mbakasir.com.features.auth.domain.LogoutApiModel
import dev.mbakasir.com.features.auth.domain.SalesHistoryApiModel
import dev.mbakasir.com.features.auth.domain.Toko
import dev.mbakasir.com.features.auth.domain.User
import dev.mbakasir.com.features.auth.domain.UserData
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler
import dev.mbakasir.com.storage.SessionHandler
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import kotlinx.coroutines.flow.first

class AuthRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val sessionHandler: SessionHandler
) : AuthRepository {
    override suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkException> {
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
    ): NetworkResult<SalesHistoryApiModel, NetworkException> {
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

    override suspend fun userInfo(): UserData {
        return UserData(
            userInfo = User(
                username = sessionHandler.getUsername().first(),
                nama = sessionHandler.getName().first(),
                role = sessionHandler.getRole().first()
            ),
            storeInfo = Toko(
                nama = sessionHandler.getStoreName().first(),
                alamat = sessionHandler.getAddress().first(),
                telp = sessionHandler.getTelp().first()
            )
        )
    }

    override suspend fun logout(): NetworkResult<LogoutApiModel, NetworkException> {
        val result = requestHandler.post<Unit, LogoutApiModel>(
            urlPathSegments = listOf("api", "logout")
        )

        if (result is NetworkResult.Success) {
            requestHandler.httpClient.authProvider<BearerAuthProvider>()?.clearToken()
            sessionHandler.clearData()
        }

        return result
    }

    override suspend fun getVersion(): NetworkResult<GetVersionApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "version")
        )
    }
}