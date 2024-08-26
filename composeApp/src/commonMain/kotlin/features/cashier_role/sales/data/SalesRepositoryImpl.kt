package features.cashier_role.sales.data

import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import kotlinx.coroutines.flow.Flow
import network.NetworkError
import network.NetworkResult
import network.RequestHandler

class SalesRepositoryImpl(
    private val mongoDB: MongoDB,
    private val requestHandler: RequestHandler
) : SalesRepository {

    override suspend fun getProductByBarcode(barcode: String): Flow<Product?> {
        return mongoDB.getProductByBarcode(barcode)
    }

    override suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkError> {
        return requestHandler.post(
            urlPathSegments = listOf("api", "penjualan", "create"),
            body = paymentRequest
        )
    }

    override suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>> {
        return mongoDB.searchProductsByBarcode(barcode)
    }
}