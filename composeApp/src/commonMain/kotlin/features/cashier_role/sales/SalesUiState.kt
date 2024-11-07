package features.cashier_role.sales

import features.cashier_role.sales.domain.ProductTransDraft

data class SalesUiState(
    val draftList: List<ProductTransDraft> = emptyList(),
)