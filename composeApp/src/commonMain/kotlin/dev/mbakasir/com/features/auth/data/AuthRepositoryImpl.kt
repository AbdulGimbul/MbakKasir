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
        val result = requestHandler.post<LoginRequest, LoginApiModel>(
            urlPathSegments = listOf("api", "login"),
            body = request
        )

        if (result is NetworkResult.Success) {
            if (result.data.code == "200") {
                val user = result.data.user
                val toko = result.data.toko
                sessionHandler.setUserData(
                    user.username,
                    user.nama,
                    user.role,
                    toko.nama,
                    toko.alamat,
                    toko.telp,
                    result.data.token
                )
            }
        }

        return result
    }

    override suspend fun isTokenValid(
        starDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<SalesHistoryApiModel, NetworkException> {
        val result = requestHandler.get<SalesHistoryApiModel>(
            urlPathSegments = listOf("api", "penjualan", "get"),
            queryParams = mapOf(
                "startDate" to starDate,
                "endDate" to endDate,
                "page" to page,
                "perPage" to perPage
            )
        )

        if (result is NetworkResult.Error) {
            if (result.exception is NetworkException.UnauthorizedException) {
                sessionHandler.clearData()
            }
        }

        return result
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
        }

        // Clear session data regardless of API result to ensure user is logged out locally
        sessionHandler.clearData()

        return result
    }

    override suspend fun getVersion(): NetworkResult<GetVersionApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "version")
        )
    }

    override suspend fun getRole(): String {
        return sessionHandler.getRole().first()
    }
}