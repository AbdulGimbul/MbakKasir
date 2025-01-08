//package storage
//
//import features.cashier_role.home.domain.ProductEntity
//import features.cashier_role.sales.domain.ProductTransEntity
//import features.cashier_role.sales.domain.ProductTransDraftEntity
//import features.cashier_role.sales.domain.copyWithNewId
//import io.realm.kotlin.Realm
//import io.realm.kotlin.RealmConfiguration
//import io.realm.kotlin.ext.query
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.flow.map
//import utils.currentTimeCustom
//import kotlin.uuid.ExperimentalUuidApi
//import kotlin.uuid.Uuid
//
//class MongoDB {
//    private var realm: Realm? = null
//
//    init {
//        configureTheRealm()
//    }
//
//    private fun configureTheRealm() {
//        if (realm == null || realm!!.isClosed()) {
//            val config = RealmConfiguration.Builder(
//                schema = setOf(ProductEntity::class, ProductTransEntity::class, ProductTransDraftEntity::class)
//            )
//                .compactOnLaunch()
//                .build()
//            realm = Realm.open(config)
//        }
//    }
//
//    suspend fun addProduct(productEntity: ProductEntity) {
//        realm?.write {
//            copyToRealm(productEntity)
//        }
//    }
//
//    suspend fun deleteProduct(productEntity: ProductEntity) {
//        realm?.write {
//            val productEntityToDelete: ProductEntity? =
//                query<ProductEntity>("id_barang == $0", productEntity.idBarang).first().find()
//            if (productEntityToDelete != null) {
//                delete(productEntityToDelete)
//            }
//        }
//    }
//
//    fun getProducts(): Flow<List<ProductEntity>> {
//        return realm?.query<ProductEntity>()
//            ?.asFlow()
//            ?.map { it.list.sortedByDescending { it.id_barang } }
//            ?: flow { throw IllegalArgumentException("Tidak ada barang") }
//    }
//
//    fun searchProductsByBarcode(query: String): Flow<List<ProductEntity>> {
//        return realm?.query<ProductEntity>(
//            "barcode CONTAINS[c] $0 OR nama_barang CONTAINS[c] $0 OR kode_barang CONTAINS[c] $0",
//            query
//        )
//            ?.asFlow()
//            ?.map { data -> data.list.sortedByDescending { it.barcode } }
//            ?: flow { emit(emptyList()) }
//    }
//
//    fun getProductByBarcode(barcode: String): Flow<ProductEntity?> {
//        return realm?.query<ProductEntity>("barcode == $0", barcode)
//            ?.first()
//            ?.asFlow()
//            ?.map { it.obj }
//            ?: flow { throw IllegalArgumentException("Tidak ada barang") }
//    }
//
//    fun isProductCacheAvailable(): Flow<Boolean> {
//        return realm?.query<ProductEntity>()?.asFlow()?.map { results ->
//            results.list.isNotEmpty()
//        } ?: flowOf(false)
//    }
//
//    fun getDrafts(): Flow<List<ProductTransDraftEntity>> {
//        return realm?.query<ProductTransDraftEntity>()
//            ?.asFlow()
//            ?.map { draft -> draft.list.sortedByDescending { it.draftId } }
//            ?: flow { emit(emptyList()) }
//    }
//
//    @OptIn(ExperimentalUuidApi::class)
//    suspend fun addProductTransToDraft(
//        draftId: String,
//        cashierName: String,
//        productTransEntity: ProductTransEntity
//    ) {
//        realm?.write {
//            val draft = query<ProductTransDraftEntity>("draftId == $0", draftId).first().find()
//
//            val clonedProduct = productTransEntity.copyWithNewId(Uuid.random().toString())
//
//            draft?.apply {
//                items.add(clonedProduct)
//            } ?: run {
//                copyToRealm(ProductTransDraftEntity().apply {
//                    this.draftId = draftId
//                    this.dateTime = currentTimeCustom()
//                    this.cashier = cashierName
//                    this.items.add(clonedProduct)
//                })
//            }
//        }
//    }
//
//    fun getProductsFromDraft(draftId: String): Flow<List<ProductTransEntity>> {
//        return realm?.query<ProductTransDraftEntity>("draftId == $0", draftId)
//            ?.asFlow()
//            ?.map { draft ->
//                draft.list.firstOrNull()?.items?.sortedByDescending { it.id_barang } ?: emptyList()
//            }
//            ?: flow { emit(emptyList()) }
//    }
//
//    suspend fun updateProductTransInDraft(
//        draftId: String,
//        productId: String? = null,
//        qty: Int? = null,
//        amountPaid: Int? = null,
//        paymentMethod: String? = null,
//        dueDate: String? = null,
//        isPrinted: Boolean? = null,
//    ) {
//        realm?.write {
//            val draft = query<ProductTransDraftEntity>("draftId == $0", draftId).first().find()
//
//            draft?.let { existingDraft ->
//                val product = existingDraft.items.firstOrNull { it.id_barang == productId }
//
//                product?.apply {
//                    if (qty != null) {
//                        qty_jual = qty
//                    }
//
//                    if (qty_jual < 1) {
//                        existingDraft.items.remove(this)
//                    }
//
//                    if (existingDraft.items.isEmpty()) {
//                        findLatest(existingDraft)?.let { latestDraft ->
//                            delete(latestDraft)
//                        }
//                    }
//                }
//
//                isPrinted?.let { existingDraft.isPrinted = it }
//                amountPaid?.let { existingDraft.amountPaid = it }
//                paymentMethod?.let { existingDraft.paymentMethod = it }
//                dueDate?.let { existingDraft.dueDate = it }
//            }
//        }
//    }
//
//    suspend fun deleteDraft(draftId: String) {
//        realm?.write {
//            try {
//                val queriedDraft = query<ProductTransDraftEntity>(query = "draftId == $0", draftId)
//                    .first()
//                    .find()
//                queriedDraft?.let {
//                    findLatest(it)?.let { currentDraft ->
//                        delete(currentDraft)
//                    }
//                }
//            } catch (e: Exception) {
//                println(e)
//            }
//        }
//    }
//}