package dev.mbakasir.com.features.cashier_role.home.data

import dev.mbakasir.com.features.cashier_role.home.domain.SalesReportApiModel
import dev.mbakasir.com.features.cashier_role.product.data.ProductDao
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val productDao: ProductDao,
    private val reqHandler: RequestHandler
) : HomeRepository {

    override suspend fun checkCache(): Flow<Boolean> {
        return productDao.isProductCacheAvailable()
    }

    override suspend fun getSalesReport(): NetworkResult<SalesReportApiModel, NetworkException> {
        return reqHandler.get(
            urlPathSegments = listOf("api", "dashboards")
        )
    }
}