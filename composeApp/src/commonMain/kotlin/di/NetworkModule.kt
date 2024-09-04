package di

import dev.mbakasir.com.BuildKonfig
import io.ktor.http.URLProtocol
import network.MbakKasirHttpClientBuilder
import network.RequestHandler
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        MbakKasirHttpClientBuilder(get())
            .protocol(URLProtocol.HTTPS)
            .host(BuildKonfig.BASE_URL)
            .build(get())
    }

    single { RequestHandler(get()) }
}