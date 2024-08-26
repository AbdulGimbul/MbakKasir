package features.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginApiModel(
    val message: String,
    val code: String,
    val token: String
)
