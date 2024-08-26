package features.auth.data

import features.auth.domain.LoginApiModel
import network.NetworkError
import network.NetworkResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkError>
}