package features.cashier_role.sales.presentation.invoice

import features.auth.domain.Toko
import features.cashier_role.sales.domain.DetailPayment

data class InvoiceUiState(
    val store: Toko? = null,
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
    val detil: List<DetailPayment> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)