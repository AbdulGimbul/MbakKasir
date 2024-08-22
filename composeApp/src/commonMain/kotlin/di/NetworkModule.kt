package di

import io.ktor.http.URLProtocol
import network.MbakKasirHttpClientBuilder
import org.koin.dsl.module

val provideHttpClientModule = module {
    single { MbakKasirHttpClientBuilder() }

    single {
        get<MbakKasirHttpClientBuilder>()
            .protocol(URLProtocol.HTTPS)
            .host("dev.mbakasir.com")
            .sessionHandler(get())
            .build(get())
    }
}