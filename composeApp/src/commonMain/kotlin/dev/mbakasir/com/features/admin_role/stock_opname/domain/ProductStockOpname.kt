package dev.mbakasir.com.features.admin_role.stock_opname.domain

data class ProductStockOpname(
    val idBarang: String = "",
    val namaBarang: String = "",
    val stok: String = "",
    val jumlah: String = "",
    val jenis: String = "Stok Masuk",
    val harga: String = "",
    val keterangan: String = "",
    val createdBy: String = ""
)