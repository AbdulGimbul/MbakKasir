package features.auth.data

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
}