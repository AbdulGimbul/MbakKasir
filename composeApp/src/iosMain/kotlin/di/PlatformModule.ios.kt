package di

import dev.bluefalcon.ApplicationContext
import dev.bluefalcon.BlueFalcon
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import storage.DatabaseFactory
import storage.createDataStore

actual val platformModule: Module
    get() = module {
        single { createDataStore() }
//        single { RemoteConfigManager() }
        single { Darwin.create() }
        single { BlueFalcon(context = ApplicationContext()) }
        single { DatabaseFactory() }
    }