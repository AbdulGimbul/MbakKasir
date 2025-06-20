package dev.mbakasir.com.features.cashier_role.product.data

import dev.mbakasir.com.features.cashier_role.product.domain.LastUpdateBarangApiModel
import dev.mbakasir.com.features.cashier_role.product.domain.ProductApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): NetworkResult<ProductApiModel, NetworkException>
    suspend fun addProduct(productEntity: ProductEntity)
    suspend fun getTopProductByStock(pageSize: Int, offset: Int): Flow<List<ProductEntity>>
    suspend fun calculateTotalProducts(): Flow<Int>
    suspend fun getLastUpdateCache(): String
    suspend fun setLastUpdateCache(lastUpdate: String)
    suspend fun getLastUpdateMaster(): NetworkResult<LastUpdateBarangApiModel, NetworkException>
    suspend fun deleteAllProducts()
}