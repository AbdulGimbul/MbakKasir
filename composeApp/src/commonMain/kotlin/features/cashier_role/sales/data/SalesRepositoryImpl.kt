package features.cashier_role.sales.data

import features.cashier_role.home.domain.ProductDao
import features.cashier_role.home.domain.ProductEntity
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.InvoiceApiModel
import features.cashier_role.sales.domain.ProductDraftWithItems
import features.cashier_role.sales.domain.ProductTransDraftDao
import features.cashier_role.sales.domain.ProductTransEntity
import features.cashier_role.sales.domain.ProductTransDraftEntity
import kotlinx.coroutines.flow.Flow
import network.NetworkException
import network.NetworkResult
import network.RequestHandler

class SalesRepositoryImpl(
    private val productDao: ProductDao,
    private val productTransDraftDao: ProductTransDraftDao,
    private val requestHandler: RequestHandler
) : SalesRepository {

    override suspend fun getProductByBarcode(barcode: String): Flow<ProductEntity?> {
        return productDao.getProductByBarcode(barcode)
    }

    override suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkException> {
        return requestHandler.post(
            urlPathSegments = listOf("api", "penjualan", "create"),
            body = paymentRequest
        )
    }

    override suspend fun searchProductsByBarcode(barcode: String): Flow<List<ProductEntity>> {
        return productDao.searchProductsByBarcode(barcode)
    }

    override suspend fun addProductTransToDraft(
        draftId: String,
        cashierName: String,
        productTransEntity: ProductTransEntity
    ) {
        return productTransDraftDao.addProductToDraft(draftId, cashierName, productTransEntity)
    }

    override suspend fun updateProductTransInDraft(
        draftId: String,
        productId: String?,
        qty: Int?,
        amountPaid: Int?,
        paymentMethod: String?,
        dueDate: String?,
        isPrinted: Boolean?
    ) {
        return productTransDraftDao.updateProductInDraft(
            draftId,
            productId,
            qty,
            amountPaid,
            paymentMethod,
            dueDate,
            isPrinted
        )
    }

    override suspend fun deleteDraft(draftId: String) {
        return productTransDraftDao.deleteDraftById(draftId)
    }

    override suspend fun getProductsFromDraft(draftId: String): Flow<List<ProductTransEntity>> {
        return productTransDraftDao.getProductsByDraftId(draftId)
    }

    override suspend fun getInvoice(invoice: String): NetworkResult<InvoiceApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "getByInvoice"),
            queryParams = mapOf("invoice" to invoice)
        )
    }

    override suspend fun getDrafts(): Flow<List<ProductDraftWithItems>> {
        return productTransDraftDao.getAllDrafts()
    }
}