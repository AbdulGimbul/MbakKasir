package features.auth.data

import features.auth.domain.LoginApiModel
import features.auth.domain.LoginRequest
import features.auth.domain.SalesHistoryApiModel
import features.auth.domain.Toko
import features.auth.domain.User
import features.auth.domain.UserData
import kotlinx.coroutines.flow.first
import network.NetworkError
import network.NetworkResult
import network.RequestHandler
import storage.SessionHandler

class AuthRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val sessionHandler: SessionHandler
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
}