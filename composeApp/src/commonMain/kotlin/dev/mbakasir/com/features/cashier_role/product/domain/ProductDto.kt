package dev.mbakasir.com.features.cashier_role.product.domain

import dev.mbakasir.com.features.admin_role.stock_opname.domain.ProductStockOpname
import dev.mbakasir.com.features.cashier_role.product.data.ProductEntity
import dev.mbakasir.com.features.cashier_role.sales.data.ProductTransEntity

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

fun ProductEntity.toProductStockOpname(createdBy: String): ProductStockOpname {
    return ProductStockOpname(
        idBarang = this.idBarang,
        namaBarang = this.namaBarang,
        stok = this.stok,
        harga = this.hargaJual,
        createdBy = createdBy
    )
}