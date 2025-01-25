package features.cashier_role.home.data

import features.cashier_role.home.domain.ProductApiModel
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult
import network.RequestHandler

class HomeRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val productDao: ProductDao
) : HomeRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barangs")
        )
    }

    override suspend fun checkCache(): Flow<Boolean> {
        return productDao.isProductCacheAvailable()
    }

    override suspend fun addProduct(productEntity: ProductEntity) {
        return productDao.addProduct(productEntity)
    }
}