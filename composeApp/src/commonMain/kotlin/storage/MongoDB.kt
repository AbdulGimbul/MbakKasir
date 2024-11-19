package storage

import features.cashier_role.home.domain.Product
import features.cashier_role.sales.domain.ProductTrans
import features.cashier_role.sales.domain.ProductTransDraft
import features.cashier_role.sales.domain.copyWithNewId
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import utils.currentTimeCustom
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MongoDB {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(Product::class, ProductTrans::class, ProductTransDraft::class)
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

    fun getDrafts(): Flow<List<ProductTransDraft>> {
        return realm?.query<ProductTransDraft>()
            ?.asFlow()
            ?.map { draft -> draft.list.sortedByDescending { it.draftId } }
            ?: flow { emit(emptyList()) }
    }

    @OptIn(ExperimentalUuidApi::class)
    suspend fun addProductTransToDraft(
        draftId: String,
        cashierName: String,
        productTrans: ProductTrans
    ) {
        realm?.write {
            val draft = query<ProductTransDraft>("draftId == $0", draftId).first().find()

            val clonedProduct = productTrans.copyWithNewId(Uuid.random().toString())

            draft?.apply {
                detail.add(clonedProduct)
            } ?: run {
                copyToRealm(ProductTransDraft().apply {
                    this.draftId = draftId
                    this.datetime = currentTimeCustom()
                    this.kasir = cashierName
                    this.detail.add(clonedProduct)
                })
            }
        }
    }

    fun getProductsFromDraft(draftId: String): Flow<List<ProductTrans>> {
        return realm?.query<ProductTransDraft>("draftId == $0", draftId)
            ?.asFlow()
            ?.map { draft ->
                draft.list.firstOrNull()?.detail?.sortedByDescending { it.id_barang } ?: emptyList()
            }
            ?: flow { emit(emptyList()) }
    }

    suspend fun updateProductTransInDraft(
        draftId: String,
        productId: String? = null,
        qty: Int? = null,
        isPrinted: Boolean? = null
    ) {
        realm?.write {
            val draft = query<ProductTransDraft>("draftId == $0", draftId).first().find()

            draft?.let { existingDraft ->
                val product = existingDraft.detail.firstOrNull { it.id_barang == productId }

                product?.apply {
                    if (qty != null) {
                        qty_jual = qty
                    }

                    if (qty_jual < 1) {
                        existingDraft.detail.remove(this)
                    }

                    if (existingDraft.detail.isEmpty()) {
                        findLatest(existingDraft)?.let { latestDraft ->
                            delete(latestDraft)
                        }
                    }
                }

                isPrinted?.let { existingDraft.isPrinted = it }
            }
        }
    }

    suspend fun deleteDraft(draftId: String) {
        realm?.write {
            try {
                val queriedDraft = query<ProductTransDraft>(query = "draftId == $0", draftId)
                    .first()
                    .find()
                queriedDraft?.let {
                    findLatest(it)?.let { currentDraft ->
                        delete(currentDraft)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}