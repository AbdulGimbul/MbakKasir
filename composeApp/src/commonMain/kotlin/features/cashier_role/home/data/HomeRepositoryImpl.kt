package features.cashier_role.home.data

import features.cashier_role.product.data.ProductDao
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val productDao: ProductDao
) : HomeRepository {

    override suspend fun checkCache(): Flow<Boolean> {
        return productDao.isProductCacheAvailable()
    }
}