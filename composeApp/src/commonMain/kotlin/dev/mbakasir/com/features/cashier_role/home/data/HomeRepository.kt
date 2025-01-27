package dev.mbakasir.com.features.cashier_role.home.data

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun checkCache(): Flow<Boolean>
}