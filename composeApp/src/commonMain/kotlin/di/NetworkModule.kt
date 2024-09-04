package di

import io.ktor.http.URLProtocol
import network.MbakKasirHttpClientBuilder
import network.RequestHandler
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        MbakKasirHttpClientBuilder(sessionHandler = get())
            .protocol(URLProtocol.HTTPS)
            .host("dev.mbakasir.com")
            .build(get())
    }

    single { RequestHandler(get()) }
}