package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import network.NetworkError
import network.NetworkResult
import network.RequestHandler

class HomeRepositoryImpl(
    private val requestHandler: RequestHandler
) : HomeRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkError> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barangs")
        )
    }
}