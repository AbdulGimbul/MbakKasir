package dev.mbakasir.com.di

import dev.mbakasir.com.BuildKonfig
import io.ktor.http.URLProtocol
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        dev.mbakasir.com.network.MbakKasirHttpClientBuilder(get())
            .protocol(URLProtocol.HTTPS)
            .host(BuildKonfig.BASE_URL)
            .build(get())
    }

    single { dev.mbakasir.com.network.RequestHandler(get()) }
}