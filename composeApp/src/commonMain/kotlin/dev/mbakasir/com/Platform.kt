package dev.mbakasir.com

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform