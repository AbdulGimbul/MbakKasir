package dev.mbakasir.com.features.admin_role.stock_opname.data

import dev.mbakasir.com.features.admin_role.stock_opname.domain.StockOpnameApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult

interface StockOpnameRepository {
    suspend fun getStockOpname(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<StockOpnameApiModel, NetworkException>
}