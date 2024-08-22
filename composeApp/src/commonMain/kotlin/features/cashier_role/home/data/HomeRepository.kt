package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import network.NetworkError
import network.NetworkResult

interface HomeRepository {
    suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkError>
}