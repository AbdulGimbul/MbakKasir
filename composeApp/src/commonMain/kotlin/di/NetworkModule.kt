package di

import io.ktor.http.URLProtocol
import network.MbakKasirHttpClientBuilder
import network.RequestHandler
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        MbakKasirHttpClientBuilder(sessionHandler = get(), remoteConfigManager = get())
            .protocol(URLProtocol.HTTPS)
            .build(get())
    }

    single { RequestHandler(get()) }
}