package features.cashier_role.sales.data

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.CreatePaymentApiModel
import features.cashier_role.sales.domain.CreatePaymentRequest
import kotlinx.coroutines.flow.Flow
import network.NetworkError
import network.NetworkResult

interface SalesRepository {
    suspend fun getProductByBarcode(barcode: String): Flow<Product?>
    suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>>
    suspend fun createPayment(paymentRequest: CreatePaymentRequest): NetworkResult<CreatePaymentApiModel, NetworkError>
}