package features.cashier_role.sales.data

import features.cashier_role.home.domain.Product
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    suspend fun getProductByBarcode(barcode: String): Flow<Product?>

    suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>>
}