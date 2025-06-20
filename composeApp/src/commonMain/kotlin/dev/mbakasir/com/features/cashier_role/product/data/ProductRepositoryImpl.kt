package dev.mbakasir.com.features.cashier_role.product.data

import dev.mbakasir.com.features.cashier_role.product.domain.LastUpdateBarangApiModel
import dev.mbakasir.com.features.cashier_role.product.domain.ProductApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler
import dev.mbakasir.com.storage.SessionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ProductRepositoryImpl(
    private val requestHandler: RequestHandler,
    private val productDao: ProductDao,
    private val sessionHandler: SessionHandler
) : ProductRepository {
    override suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barangs")
        )
    }

    override suspend fun addProduct(productEntity: ProductEntity) {
        return productDao.addProduct(productEntity)
    }

    override suspend fun getTopProductByStock(
        pageSize: Int,
        offset: Int
    ): Flow<List<ProductEntity>> {
        return productDao.getTopProductsByStok(pageSize, offset)
    }

    override suspend fun calculateTotalProducts(): Flow<Int> {
        return productDao.calculateTotalProducts()
    }

    override suspend fun getLastUpdateCache(): String {
        return sessionHandler.getLastUpdate().first()
    }

    override suspend fun setLastUpdateCache(lastUpdate: String) {
        sessionHandler.setLastUpdate(lastUpdate)
    }

    override suspend fun getLastUpdateMaster(): NetworkResult<LastUpdateBarangApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "barang", "lastUpdate")
        )
    }
}