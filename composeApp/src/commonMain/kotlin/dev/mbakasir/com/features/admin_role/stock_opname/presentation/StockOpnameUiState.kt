package dev.mbakasir.com.features.admin_role.stock_opname.presentation

import dev.mbakasir.com.features.admin_role.stock_opname.domain.StockOpnameApiModel

data class StockOpnameUiState(
    val startDate: String = "",
    val endDate: String = "",
    val stockOpname: StockOpnameApiModel? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)