package features.cashier_role.product.domain

import features.cashier_role.product.data.ProductEntity
import features.cashier_role.sales.data.ProductTransEntity

fun ProductEntity.toProductTrans(draftId: String): ProductTransEntity {
    return ProductTransEntity(
        draftId = draftId,
        idBarang = this.idBarang,
        kodeBarang = this.kodeBarang,
        barcode = this.barcode,
        namaBarang = this.namaBarang,
        qtyJual = 1,
        hargaItem = this.hargaJual.toIntOrNull() ?: 0
    )
}