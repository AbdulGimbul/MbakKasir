package features.cashier_role.sales.data

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.InvoiceApiModel
import features.cashier_role.sales.domain.ProductTrans
import features.cashier_role.sales.domain.ProductTransDraft
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult

interface SalesRepository {
    suspend fun getProductByBarcode(barcode: String): Flow<Product?>
    suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>>
    suspend fun addProductTransToDraft(draftId: String, cashierName: String, productTrans: ProductTrans)
    suspend fun getProductsFromDraft(draftId: String): Flow<List<ProductTrans>>
    suspend fun updateProductTransInDraft(draftId: String, productId: String, qty: Int)
    suspend fun deleteDraft(draftId: String)
    suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkException>
    suspend fun getInvoice(invoice: String): NetworkResult<InvoiceApiModel, NetworkException>
    suspend fun getDrafts(): Flow<List<ProductTransDraft>>
}