package dev.mbakasir.com.features.cashier_role.sales.data

import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentRequest
import dev.mbakasir.com.features.cashier_role.sales.domain.HistoryApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.InvoiceApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.PelangganApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    suspend fun getProductByBarcode(barcode: String): Flow<ProductEntity?>
    suspend fun searchProductsByBarcode(barcode: String): Flow<List<ProductEntity>>
    suspend fun addProductTransToDraft(
        draftId: String,
        cashierName: String,
        productTransEntity: ProductTransEntity
    )

    suspend fun getProductsFromDraft(draftId: String): Flow<List<ProductTransEntity>>
    suspend fun updateProductTransInDraft(
        draftId: String,
        productId: String? = null,
        qty: Int? = null,
        amountPaid: Int? = null,
        paymentMethod: String? = null,
        dueDate: String? = null,
        isPrinted: Boolean? = null
    )

    suspend fun deleteDraft(draftId: String)
    suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkException>
    suspend fun getInvoice(invoice: String): NetworkResult<InvoiceApiModel, NetworkException>
    suspend fun getDrafts(): Flow<List<ProductDraftWithItems>>
    suspend fun getHistory(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<HistoryApiModel, NetworkException>

    suspend fun getCustomers(): NetworkResult<PelangganApiModel, NetworkException>
}