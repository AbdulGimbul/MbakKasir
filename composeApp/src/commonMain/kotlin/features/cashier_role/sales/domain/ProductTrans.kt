package features.cashier_role.sales.domain

data class ProductTrans(
    val id_barang: String,
    val kode_barang: String,
    val barcode: String,
    val nama_barang: String,
    val id_karyawan: String = "",
    val jenis: String = "Produk",
    val qty_jual: Int = 1,
    val harga_item: Int = 0,
    val diskon: Int = 0,
    val subtotal: Int = qty_jual * harga_item - diskon,
)

fun ProductTrans.toDetailPayload(): DetailPayload {
    return DetailPayload(
        id_barang = this.id_barang,
        id_karyawan = this.id_karyawan,
        jenis = this.jenis,
        qty_jual = this.qty_jual.toString(),
        harga_item = this.harga_item.toString(),
        subtotal = this.subtotal.toString(),
        diskon = this.diskon
    )
}