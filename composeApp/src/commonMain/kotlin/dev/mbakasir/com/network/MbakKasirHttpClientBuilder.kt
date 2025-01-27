package dev.mbakasir.com.network

import dev.mbakasir.com.storage.SessionHandler
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class MbakKasirHttpClientBuilder(
    private val sessionHandler: SessionHandler
) {

    private lateinit var protocol: URLProtocol
    private lateinit var host: String
    private var port: Int? = null

    fun protocol(protocol: URLProtocol) = apply { this.protocol = protocol }

    fun host(host: String) = apply { this.host = host }

    fun port(port: Int) = apply { this.port = port }

    fun build(engine: HttpClientEngine): HttpClient {
        return HttpClient(engine) {

            expectSuccess = true

            defaultRequest {
                url {
                    protocol = this@MbakKasirHttpClientBuilder.protocol
                    host = this@MbakKasirHttpClientBuilder.host
                    this@MbakKasirHttpClientBuilder.port?.let { port = it }
                }

                header("Content-Type", "application/json")
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            accessToken = sessionHandler.getToken().first(),
                            refreshToken = ""
                        )
                    }
                }
            }
        }
    }
}