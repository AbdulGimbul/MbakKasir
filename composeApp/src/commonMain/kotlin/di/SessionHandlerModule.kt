package di

import org.koin.dsl.module
import storage.SessionHandler

val provideSessionHandlerModule = module {
    single { SessionHandler(get()) }
}