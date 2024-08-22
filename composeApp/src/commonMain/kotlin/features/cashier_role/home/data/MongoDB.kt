package features.cashier_role.home.data

import features.cashier_role.home.domain.Product
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class MongoDB {
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(Product::class)
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
            try {
                val queriedProduct = query<Product>(query = "id_barang == $0", product.id_barang)
                    .first()
                    .find()
                queriedProduct?.let { currentProduct ->
                    delete(currentProduct)
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}