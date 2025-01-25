package features.cashier_role.product.data

import features.cashier_role.product.domain.ProductApiModel
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult

interface ProductRepository {
    suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException>
    suspend fun addProduct(productEntity: ProductEntity)
    suspend fun getTopProductByStock(): Flow<List<ProductEntity>>
    suspend fun calculateTotalProducts(): Flow<Int>
}