package dev.mbakasir.com.features.cashier_role.product.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastUpdateBarangApiModel(
    @SerialName("last_update")
    val lastUpdate: String?,
    val code: String
)