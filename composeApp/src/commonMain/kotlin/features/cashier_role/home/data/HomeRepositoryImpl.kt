package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import network.NetworkError
import network.NetworkResult

class HomeRepositoryImpl(
    private val httpClient: HttpClient
) : HomeRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "https://dev.mbakasir.com/api/barangs/"
            )
        } catch (e: UnresolvedAddressException) {
            return NetworkResult.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return NetworkResult.Error(NetworkError.SERIALIZATION)
        }

        return when (response.status.value) {
            in 200..299 -> {
                val result = response.body<ProductApiModel>()
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