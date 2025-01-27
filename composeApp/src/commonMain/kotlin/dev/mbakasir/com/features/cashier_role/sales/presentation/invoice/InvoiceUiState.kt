package dev.mbakasir.com.features.cashier_role.sales.presentation.invoice

data class InvoiceUiState(
    val store: dev.mbakasir.com.features.auth.domain.Toko? = null,
    val totalHarga: Double = 0.0,
    val diskon: Double = 0.0,
    val subtotal: Double = 0.0,
    val invoiceNumber: String = "",
    val tanggal: String = "",
    val method: String = "",
    val kasir: String = "",
    val ppn: Double = 0.0,
    val bayar: Double = 0.0,
    val kembali: Double = 0.0,
    val detil: List<dev.mbakasir.com.features.cashier_role.sales.domain.DetailPayment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)