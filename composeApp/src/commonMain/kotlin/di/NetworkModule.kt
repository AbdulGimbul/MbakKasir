package di

import io.ktor.http.URLProtocol
import network.MbakKasirHttpClientBuilder
import network.RequestHandler
import org.koin.dsl.module

val provideHttpClientModule = module {
    factory {
        MbakKasirHttpClientBuilder(get())
            .protocol(URLProtocol.HTTPS)
            .host("dev.mbakasir.com")
            .build(get())
    }

    single { RequestHandler(get()) }
}