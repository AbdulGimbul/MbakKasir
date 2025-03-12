package dev.mbakasir.com.features.admin_role.stock_opname.data

import dev.mbakasir.com.features.admin_role.stock_opname.domain.StockOpnameApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler

class StockOpnameRepositoryImpl(
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
}