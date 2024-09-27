package features.cashier_role.sales.data

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import features.cashier_role.sales.domain.InvoiceApiModel
import features.cashier_role.sales.domain.ProductTrans
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

    override suspend fun addProductTrans(productTrans: ProductTrans) {
        return mongoDB.addProductTrans(productTrans)
    }

    override suspend fun updateProductTrans(
        product: ProductTrans,
        qty: Int,
    ) {
        return mongoDB.updateProductTrans(product, qty)
    }

    override suspend fun deleteProductTrans(productId: String) {
        return mongoDB.deleteProductTrans(productId)
    }

    override suspend fun getScannedProducts(): Flow<List<ProductTrans>> {
        return mongoDB.getProductsTrans()
    }

    override suspend fun getInvoice(invoice: String): NetworkResult<InvoiceApiModel, NetworkException> {
        return requestHandler.get(
            urlPathSegments = listOf("api", "penjualan", "getByInvoice"),
            queryParams = mapOf("invoice" to invoice)
        )
    }
}