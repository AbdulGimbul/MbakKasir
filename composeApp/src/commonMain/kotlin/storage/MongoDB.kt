package storage

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.ProductTrans
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class MongoDB {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(Product::class, ProductTrans::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    suspend fun addProduct(product: Product) {
        realm?.write {
            copyToRealm(product)
        }
    }

    suspend fun deleteProduct(product: Product) {
        realm?.write {
            val productToDelete: Product? =
                query<Product>("id_barang == $0", product.id_barang).first().find()
            if (productToDelete != null) {
                delete(productToDelete)
            }
        }
    }

    fun getProducts(): Flow<List<Product>> {
        return realm?.query<Product>()
            ?.asFlow()
            ?.map { it.list.sortedByDescending { it.id_barang } }
            ?: flow { throw IllegalArgumentException("Tidak ada barang") }
    }

    fun searchProductsByBarcode(query: String): Flow<List<Product>> {
        return realm?.query<Product>(
            "barcode CONTAINS[c] $0 OR nama_barang CONTAINS[c] $0 OR kode_barang CONTAINS[c] $0",
            query
        )
            ?.asFlow()
            ?.map { data -> data.list.sortedByDescending { it.barcode } }
            ?: flow { throw IllegalArgumentException("Tidak ada barang") }
    }

    fun getProductByBarcode(barcode: String): Flow<Product?> {
        return realm?.query<Product>("barcode == $0", barcode)
            ?.first()
            ?.asFlow()
            ?.map { it.obj }
            ?: flow { throw IllegalArgumentException("Tidak ada barang") }
    }

    fun isProductCacheAvailable(): Flow<Boolean> {
        return realm?.query<Product>()?.asFlow()?.map { results ->
            results.list.isNotEmpty()
        } ?: flowOf(false)
    }

    suspend fun addProductTrans(productTrans: ProductTrans) {
        realm?.write {
            copyToRealm(productTrans)
        }
    }

    fun getProductsTrans(): Flow<List<ProductTrans>> {
        return realm?.query<ProductTrans>()
            ?.asFlow()
            ?.map { trans -> trans.list.sortedByDescending { it.id_barang } }
            ?: flow { emit(emptyList()) }
    }

    suspend fun updateProductTrans(product: ProductTrans, qty: Int) {
        realm?.write {
            try {
                val queriedTask = query<ProductTrans>("id_barang == $0", product.id_barang)
                    .find()
                    .first()
                queriedTask.apply {
                    qty_jual = qty

                    if (qty_jual < 1) {
                        delete(this)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    suspend fun deleteProductTrans(product: ProductTrans) {
        realm?.write {
            try {
                val queriedTask = query<ProductTrans>(query = "id_barang == $0", product.id_barang)
                    .first()
                    .find()
                queriedTask?.let {
                    findLatest(it)?.let { currentTask ->
                        delete(currentTask)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}