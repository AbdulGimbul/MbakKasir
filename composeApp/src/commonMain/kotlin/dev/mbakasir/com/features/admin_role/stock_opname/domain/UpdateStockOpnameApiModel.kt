package dev.mbakasir.com.features.admin_role.stock_opname.domain

import kotlinx.serialization.Serializable

@Serializable
data class UpdateStockOpnameApiModel(
    val message: String,
    val code: String
)
