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
import network.RequestHandler
import storage.MongoDB

class SalesRepositoryImpl(
    private val mongoDB: MongoDB,
    private val requestHandler: RequestHandler
) : SalesRepository {

    override suspend fun getProductByBarcode(barcode: String): Flow<Product?> {
        return mongoDB.getProductByBarcode(barcode)
    }

    override suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkException> {
        return requestHandler.post(
            urlPathSegments = listOf("api", "penjualan", "create"),
            body = paymentRequest
        )
    }

    override suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>> {
        return mongoDB.searchProductsByBarcode(barcode)
    }

    override suspend fun addProductTransToDraft(
        draftId: String,
        cashierName: String,
        productTrans: ProductTrans
    ) {
        return mongoDB.addProductTransToDraft(draftId, cashierName, productTrans)
    }

    override suspend fun updateProductTransInDraft(
        draftId: String,
        productId: String?,
        qty: Int?,
        isPrinted: Boolean?
    ) {
        return mongoDB.updateProductTransInDraft(draftId, productId, qty, isPrinted)
    }

    override suspend fun deleteDraft(draftId: String) {
        return mongoDB.deleteDraft(draftId)
    }

    override suspend fun getProductsFromDraft(draftId: String): Flow<List<ProductTrans>> {
        return mongoDB.getProductsFromDraft(draftId)
    }

    override suspend fun getInvoice(invoice: String): NetworkResult<InvoiceApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "getByInvoice"),
            queryParams = mapOf("invoice" to invoice)
        )
    }

    override suspend fun getDrafts(): Flow<List<ProductTransDraft>> {
        return mongoDB.getDrafts()
    }
}