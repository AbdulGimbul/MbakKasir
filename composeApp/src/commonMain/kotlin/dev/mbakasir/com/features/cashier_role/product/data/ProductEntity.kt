package dev.mbakasir.com.features.cashier_role.product.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@Entity(tableName = "products")
data class ProductEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey var idBarang: String,
    var kodeBarang: String = "",
    var barcode: String = "",
    var namaBarang: String = "",
    var satuan: String = "",
    var hargaJual: String = "",
    var hargaPelanggan: String = "",
    var hargaToko: String = "",
    var hargaSales: String = "",
    var stok: String = "",
    val createdAt: LocalDateTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
)
