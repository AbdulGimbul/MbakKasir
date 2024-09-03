package di

import io.ktor.client.engine.darwin.Darwin
import network.RemoteConfigManager
import org.koin.core.module.Module
import org.koin.dsl.module
import storage.createDataStore

actual val platformModule: Module
    get() = module {
        single { createDataStore() }
        single { RemoteConfigManager() }
        single { Darwin.create() }
    }