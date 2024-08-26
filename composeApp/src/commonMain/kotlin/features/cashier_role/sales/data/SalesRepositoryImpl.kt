package features.cashier_role.sales.data

import features.cashier_role.home.data.MongoDB
import features.cashier_role.home.domain.Product
import kotlinx.coroutines.flow.Flow

class SalesRepositoryImpl(
    private val mongoDB: MongoDB
) : SalesRepository {

    override suspend fun getProductByBarcode(barcode: String): Flow<Product?> {
        return mongoDB.getProductByBarcode(barcode)
    }

    override suspend fun searchProductsByBarcode(barcode: String): Flow<List<Product>> {
        return mongoDB.searchProductsByBarcode(barcode)
    }
}