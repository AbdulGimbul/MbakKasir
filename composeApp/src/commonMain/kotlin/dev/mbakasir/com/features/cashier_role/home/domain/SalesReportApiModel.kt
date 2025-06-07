package dev.mbakasir.com.features.cashier_role.home.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SalesReportApiModel(
    @SerialName("nominal")
    val nominalSales: SalesDataItem,

    @SerialName("penjualan")
    val totalSales: SalesDataItem,

    @SerialName("pembeli")
    val totalCustomers: SalesDataItem,

    val code: String
)

@Serializable
data class SalesDataItem(
    val name: String,
    val data: String
)