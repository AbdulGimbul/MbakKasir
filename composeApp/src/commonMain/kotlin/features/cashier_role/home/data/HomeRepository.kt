package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult

interface HomeRepository {
    suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException>
    suspend fun checkCache(): Flow<Boolean>
    suspend fun addProduct(productEntity: ProductEntity)
}