package dev.mbakasir.com.features.cashier_role.product.presentation

import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

data class ProductUiState @OptIn(ExperimentalTime::class) constructor(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val paymentResponse: CreatePaymentApiModel? = null,
    val productList: List<ProductEntity> = emptyList(),
    val totalProduct: Int = 0,
    val latestUpdate: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
)