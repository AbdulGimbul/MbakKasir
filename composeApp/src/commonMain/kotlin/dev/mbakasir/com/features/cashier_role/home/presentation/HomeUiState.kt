package dev.mbakasir.com.features.cashier_role.home.presentation

data class HomeUiState(
    val nominalPenjualan: String = "",
    val jumlahPenjualan: String = "",
    val jumlahPembeli: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)