package dev.mbakasir.com.network

sealed class NetworkException(message: String, cause: Throwable) : Exception(message, cause) {

    // Common exceptions
    data class UnauthorizedException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class NotFoundException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class UnknownException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class NoInternetException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class SerializationException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    // Less common exceptions (commented out)
    /*
    data class RequestTimeoutException(
        override val message: String,
        override val cause: Throwable
    ) : NetworkException(message, cause)

    data class TooManyRequestsException(
        override val message: String,
        override val cause: Throwable
    ) : NetworkException(message, cause)

    data class PayloadTooLargeException(
        override val message: String,
        override val cause: Throwable
    ) : NetworkException(message, cause)

    data class ServerErrorException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class ForbiddenException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)

    data class BadRequestException(override val message: String, override val cause: Throwable) :
        NetworkException(message, cause)
    */
}

fun NetworkException.toUiMessage(): String {
    return when (this) {
        is NetworkException.UnauthorizedException -> "Unauthorized"
        is NetworkException.NotFoundException -> "Not Found"
        is NetworkException.UnknownException -> "Unknown Error"
        is NetworkException.NoInternetException -> "No Internet"
        is NetworkException.SerializationException -> "Serialization Error"
    }
}