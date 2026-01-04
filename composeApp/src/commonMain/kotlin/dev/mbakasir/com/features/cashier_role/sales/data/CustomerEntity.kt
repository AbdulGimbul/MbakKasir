package dev.mbakasir.com.features.cashier_role.sales.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.mbakasir.com.features.cashier_role.sales.domain.Customer

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey val kode: String,
    val nama: String,
    val telp: String,
    val email: String,
    val alamat: String,
    val jenis_cs: String
)

fun CustomerEntity.toDomain(): Customer {
    return Customer(
        kode = kode,
        nama = nama,
        telp = telp,
        email = email,
        alamat = alamat,
        jenis_cs = jenis_cs
    )
}

fun Customer.toEntity(): CustomerEntity {
    return CustomerEntity(
        kode = kode,
        nama = nama,
        telp = telp,
        email = email,
        alamat = alamat,
        jenis_cs = jenis_cs
    )
}
