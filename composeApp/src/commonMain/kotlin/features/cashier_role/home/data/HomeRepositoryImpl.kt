package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
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