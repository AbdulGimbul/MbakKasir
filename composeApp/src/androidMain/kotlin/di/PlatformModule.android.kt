package di

import io.ktor.client.engine.okhttp.OkHttp
import network.RemoteConfigManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import storage.createDataStore

actual val platformModule: Module
    get() = module {
        single { createDataStore(context = androidContext()) }
        single { RemoteConfigManager() }
        single { OkHttp.create() }
    }