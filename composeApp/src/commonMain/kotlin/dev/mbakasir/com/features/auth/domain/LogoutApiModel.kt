package dev.mbakasir.com.features.auth.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogoutApiModel(
    val code: String,
    @SerialName("JWT_SECRET")
    val jwtSecret: String
)