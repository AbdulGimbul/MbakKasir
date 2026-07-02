package dev.mbakasir.com.features.admin_role.product.presentation

import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel

data class ProductUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: CreatePaymentApiModel? = null,
    val productList: List<ProductEntity> = emptyList(),
    val totalProduct: Int = 0,
    val latestUpdate: String = "Belum ada update",
)
