package dev.mbakasir.com.di

import com.plusmobileapps.konnectivity.Konnectivity
import dev.mbakasir.com.BuildKonfig
import dev.mbakasir.com.network.MbakKasirHttpClientBuilder
import dev.mbakasir.com.network.RequestHandler
import io.ktor.http.URLProtocol
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        MbakKasirHttpClientBuilder(get())
            .protocol(URLProtocol.HTTPS)
            .host(BuildKonfig.BASE_URL)
            .build(get())
    }

    single { RequestHandler(get()) }

    single { Konnectivity() }
}