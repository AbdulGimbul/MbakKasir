package di

import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import storage.createDataStore

actual val platformModule: Module
    get() = module {
        single { createDataStore() }
        single { Darwin.create() }
    }