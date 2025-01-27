package dev.mbakasir.com.features.cashier_role.product.data

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): dev.mbakasir.com.network.NetworkResult<dev.mbakasir.com.features.cashier_role.product.domain.ProductApiModel, dev.mbakasir.com.network.NetworkException>
    suspend fun addProduct(productEntity: ProductEntity)
    suspend fun getTopProductByStock(): Flow<List<ProductEntity>>
    suspend fun calculateTotalProducts(): Flow<Int>
}