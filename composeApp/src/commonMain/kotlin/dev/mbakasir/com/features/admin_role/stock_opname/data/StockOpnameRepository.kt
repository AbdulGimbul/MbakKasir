package dev.mbakasir.com.features.admin_role.stock_opname.data

import dev.mbakasir.com.features.admin_role.stock_opname.domain.StockOpnameApiModel
import dev.mbakasir.com.features.admin_role.stock_opname.domain.UpdateStockOpnameApiModel
import dev.mbakasir.com.features.admin_role.stock_opname.domain.UpdateStockOpnameReq
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.network.NetworkException
import dev.mbakasir.com.network.NetworkResult
import kotlinx.coroutines.flow.Flow

interface StockOpnameRepository {
    suspend fun getStockOpname(
        startDate: String,
        endDate: String,
        page: String,
        perPage: String
    ): NetworkResult<StockOpnameApiModel, NetworkException>

    suspend fun updateStockOpname(req: UpdateStockOpnameReq): NetworkResult<UpdateStockOpnameApiModel, NetworkException>

    suspend fun getProductByBarcode(barcode: String): Flow<ProductEntity?>
    suspend fun searchProductsByBarcode(barcode: String): Flow<List<ProductEntity>>
    suspend fun updateStokById(id: String, newStok: String)
}