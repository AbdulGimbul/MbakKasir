package network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
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
    ): NetworkResult<R, NetworkError> {
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

                NetworkResult.Success(response)
            } catch (e: ResponseException) {
                when (e.response.status) {
                    HttpStatusCode.Unauthorized -> NetworkResult.Error(NetworkError.UNAUTHORIZED)
                    HttpStatusCode.Forbidden -> NetworkResult.Error(NetworkError.UNAUTHORIZED)
                    HttpStatusCode.NotFound -> NetworkResult.Error(NetworkError.UNKNOWN)
                    HttpStatusCode.BadRequest -> NetworkResult.Error(NetworkError.UNKNOWN)
                    HttpStatusCode.RequestTimeout -> NetworkResult.Error(NetworkError.REQUEST_TIMEOUT)
                    HttpStatusCode.TooManyRequests -> NetworkResult.Error(NetworkError.TOO_MANY_REQUESTS)
                    HttpStatusCode.PayloadTooLarge -> NetworkResult.Error(NetworkError.PAYLOAD_TOO_LARGE)
                    in HttpStatusCode.InternalServerError..HttpStatusCode.GatewayTimeout -> NetworkResult.Error(
                        NetworkError.SERVER_ERROR
                    )

                    else -> NetworkResult.Error(NetworkError.UNKNOWN)
                }
            } catch (e: UnresolvedAddressException) {
                NetworkResult.Error(NetworkError.NO_INTERNET)
            } catch (e: SerializationException) {
                NetworkResult.Error(NetworkError.SERIALIZATION)
            } catch (e: Exception) {
                NetworkResult.Error(NetworkError.UNKNOWN)
            }
        }
    }

    suspend inline fun <reified R> get(
        urlPathSegments: List<Any>,
        queryParams: Map<String, Any>? = null
    ): NetworkResult<R, NetworkError> = executeRequest<Any, R>(
        method = HttpMethod.Get,
        urlPathSegments = urlPathSegments.toList(),
        queryParams = queryParams
    )

    suspend inline fun <reified B, reified R> post(
        urlPathSegments: List<Any>,
        body: B? = null
    ): NetworkResult<R, NetworkError> = executeRequest(
        method = HttpMethod.Post,
        urlPathSegments = urlPathSegments.toList(),
        body = body
    )

    suspend inline fun <reified B, reified R> put(
        urlPathSegments: List<Any>,
        body: B? = null
    ): NetworkResult<R, NetworkError> = executeRequest(
        method = HttpMethod.Put,
        urlPathSegments = urlPathSegments.toList(),
        body = body
    )
}