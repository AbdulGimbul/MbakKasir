package dev.mbakasir.com.features.admin_role.stock_opname.data

import dev.mbakasir.com.features.admin_role.stock_opname.domain.StockOpnameApiModel
import dev.mbakasir.com.features.admin_role.stock_opname.domain.UpdateStockOpnameApiModel
import dev.mbakasir.com.features.admin_role.stock_opname.domain.UpdateStockOpnameReq
import dev.mbakasir.com.features.cashier_role.product.data.ProductDao
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler
import kotlinx.coroutines.flow.Flow

class StockOpnameRepositoryImpl(
    private val productDao: ProductDao,
    private val requestHandler: RequestHandler
) : StockOpnameRepository {
    override suspend fun getStockOpname(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<StockOpnameApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "stok", "get"),
            queryParams = mapOf(
                "startDate" to startDate,
                "endDate" to endDate,
                "page" to page,
                "perPage" to perPage
            )
        )
    }

    override suspend fun updateStockOpname(req: UpdateStockOpnameReq): NetworkResult<UpdateStockOpnameApiModel, NetworkException> {
        return requestHandler.post(
            urlPathSegments = listOf("api", "stok", "update"),
            body = req
        )
    }

    override suspend fun searchProductsByBarcode(barcode: String): Flow<List<ProductEntity>> {
        return productDao.searchProductsByBarcode(barcode)
    }

    override suspend fun getProductByBarcode(barcode: String): Flow<ProductEntity?> {
        return productDao.getProductByBarcode(barcode)
    }
}