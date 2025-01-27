package dev.mbakasir.com.features.cashier_role.product.data

import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val requestHandler: dev.mbakasir.com.network.RequestHandler,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProducts(): dev.mbakasir.com.network.NetworkResult<dev.mbakasir.com.features.cashier_role.product.domain.ProductApiModel, dev.mbakasir.com.network.NetworkException> {
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