package dev.mbakasir.com.features.auth.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginApiModel(
    val message: String,
    val user: User,
    val toko: Toko,
    val code: String,
    val token: String
)

@Serializable
data class User(
    val username: String,
    val nama: String,
    val role: String
)

@Serializable
data class Toko(
    val nama: String,
    val alamat: String,
    val telp: String
)

@Serializable
data class GetVersionApiModel(
    @SerialName("Name")
    val name: String,
    @SerialName("Version")
    val version: String,
    @SerialName("Website")
    val website: String
)