package dev.mbakasir.com.features.admin_role.stock_opname.domain

fun ProductStockOpname.toUpdateStockOpnameReq(): UpdateStockOpnameReq {
    return UpdateStockOpnameReq(
        idBarang = idBarang,
        jenis = jenis,
        harga = harga,
        jumlah = jumlah,
        keterangan = keterangan,
        createdBy = createdBy
    )
}