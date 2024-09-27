package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import network.NetworkException
import network.NetworkResult
import network.RequestHandler

class HomeRepositoryImpl(
    private val requestHandler: RequestHandler
) : HomeRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barangs")
        )
    }
}