package features.cashier_role.sales.data

import features.cashier_role.home.data.ProductEntity
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.InvoiceApiModel
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult

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
}