package features.auth.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import network.NetworkError
import network.NetworkResult

class AuthRepositoryImpl(
    private val httpClient: HttpClient
) : AuthRepository {
    override suspend fun login(request: LoginRequest): NetworkResult<LoginApiModel, NetworkError> {
        val response = try {
            httpClient.post(
                urlString = "https://dev.mbakasir.com/api/login"
            ) {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
        } catch (e: UnresolvedAddressException) {
            return NetworkResult.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return NetworkResult.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val result = response.body<LoginApiModel>()
                NetworkResult.Success(result)
            }

            401 -> NetworkResult.Error(NetworkError.UNAUTHORIZED)
            409 -> NetworkResult.Error(NetworkError.CONFLICT)
            408 -> NetworkResult.Error(NetworkError.REQUEST_TIMEOUT)
            413 -> NetworkResult.Error(NetworkError.PAYLOAD_TOO_LARGE)
            in 500..599 -> NetworkResult.Error(NetworkError.SERVER_ERROR)
            else -> NetworkResult.Error(NetworkError.UNKNOWN)
        }
    }
}