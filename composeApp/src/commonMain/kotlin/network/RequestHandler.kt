package network

import features.auth.domain.LoginRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.client.request.parameter
import io.ktor.client.request.prepareRequest
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException

class RequestHandler(val httpClient: HttpClient) {

    suspend inline fun <reified B, reified R> executeRequest(
        method: HttpMethod,
        urlPathSegments: List<Any>,
        body: B? = null,
        queryParams: Map<String, Any>? = null
    ): NetworkResult<R, NetworkException> {
        return withContext(Dispatchers.IO) {
            try {
                val response = httpClient.prepareRequest {
                    this.method = method
                    url {
                        val pathSegments = urlPathSegments.map { it.toString() }
                        appendPathSegments(pathSegments)
                    }
                    body?.let { setBody(it) }
                    queryParams?.let { params ->
                        params.forEach { (key, value) ->
                            parameter(key, value)
                        }
                    }
                }.execute().body<R>()

                if (body is LoginRequest) {
                    httpClient.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>().first()
                        .clearToken()
                }

                NetworkResult.Success(response)
            } catch (e: ResponseException) {
                val contentType = e.response.headers["Content-Type"]
                val errorBody: DefaultError? =
                    if (contentType?.contains("application/json") == true) {
                        e.response.body<DefaultError>()
                    } else {
                        null
                    }
                val networkException = when (e.response.status) {
                    HttpStatusCode.Unauthorized -> NetworkException.UnauthorizedException(
                        errorBody?.message ?: "Unauthorized",
                        e
                    )

                    HttpStatusCode.NotFound -> NetworkException.NotFoundException(
                        errorBody?.message ?: "Not Found",
                        e
                    )

                    else -> NetworkException.UnknownException("Error: ${e.response.status}", e)
                }
                NetworkResult.Error(networkException)
            } catch (e: UnresolvedAddressException) {
                NetworkResult.Error(NetworkException.NoInternetException("no internet", e))
            } catch (e: SerializationException) {
                NetworkResult.Error(
                    NetworkException.SerializationException(
                        "serialization error",
                        e
                    )
                )
            } catch (e: Exception) {
                NetworkResult.Error(NetworkException.UnknownException("unknown error", e))
            }
        }
    }

    suspend inline fun <reified R> get(
        urlPathSegments: List<Any>,
        queryParams: Map<String, Any>? = null
    ): NetworkResult<R, NetworkException> = executeRequest<Any, R>(
        method = HttpMethod.Get,
        urlPathSegments = urlPathSegments.toList(),
        queryParams = queryParams
    )

    suspend inline fun <reified B, reified R> post(
        urlPathSegments: List<Any>,
        body: B? = null
    ): NetworkResult<R, NetworkException> = executeRequest(
        method = HttpMethod.Post,
        urlPathSegments = urlPathSegments.toList(),
        body = body
    )

    suspend inline fun <reified B, reified R> put(
        urlPathSegments: List<Any>,
        body: B? = null
    ): NetworkResult<R, NetworkException> = executeRequest(
        method = HttpMethod.Put,
        urlPathSegments = urlPathSegments.toList(),
        body = body
    )
}