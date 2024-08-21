package di

import network.createHttpClient
import org.koin.dsl.module

val provideHttpClientModule = module {
    single { createHttpClient(sessionHandler = get(), engine = get()) }
}