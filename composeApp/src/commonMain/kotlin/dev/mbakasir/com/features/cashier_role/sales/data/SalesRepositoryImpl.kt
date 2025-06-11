package dev.mbakasir.com.features.cashier_role.sales.data

import dev.mbakasir.com.features.cashier_role.product.data.ProductDao
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.CreatePaymentRequest
import dev.mbakasir.com.features.cashier_role.sales.domain.HistoryApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.InvoiceApiModel
import dev.mbakasir.com.features.cashier_role.sales.domain.PelangganApiModel
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import dev.mbakasir.com.network.RequestHandler
import kotlinx.coroutines.flow.Flow

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
        isPrinted: Boolean?,
        customer: String?
    ) {
        return productTransDraftDao.updateProductInDraft(
            draftId,
            productId,
            qty,
            amountPaid,
            paymentMethod,
            dueDate,
            isPrinted,
            customer
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

    override suspend fun getHistory(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<HistoryApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "get"),
            queryParams = mapOf(
                "startDate" to startDate,
                "endDate" to endDate,
                "page" to page,
                "perPage" to perPage
            )
        )
    }

    override suspend fun getCustomers(): NetworkResult<PelangganApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "pelanggan", "p", "all")
        )
    }
}