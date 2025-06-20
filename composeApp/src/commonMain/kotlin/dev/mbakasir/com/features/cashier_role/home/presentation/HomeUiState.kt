package dev.mbakasir.com.features.cashier_role.home.presentation

import dev.mbakasir.com.features.auth.domain.UserData

data class HomeUiState(
    val user: UserData? = null,
    val nominalPenjualan: String = "",
    val jumlahPenjualan: String = "",
    val jumlahPembeli: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)