package features.cashier_role.product.data

import features.cashier_role.product.domain.ProductApiModel
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult
import network.RequestHandler

class ProductRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barangs")
        )
    }

    override suspend fun addProduct(productEntity: ProductEntity) {
        return productDao.addProduct(productEntity)
    }

    override suspend fun getTopProductByStock(): Flow<List<ProductEntity>> {
        return productDao.getTopProductsByStok()
    }

    override suspend fun calculateTotalProducts(): Flow<Int> {
        return productDao.calculateTotalProducts()
    }
}