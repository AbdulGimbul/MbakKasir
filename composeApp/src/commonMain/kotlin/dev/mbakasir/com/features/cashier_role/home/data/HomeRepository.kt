package dev.mbakasir.com.features.cashier_role.home.data

import dev.mbakasir.com.features.cashier_role.home.domain.SalesReportApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun checkCache(): Flow<Boolean>
    suspend fun getSalesReport(): NetworkResult<SalesReportApiModel, NetworkException>

}