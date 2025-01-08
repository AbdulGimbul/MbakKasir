package di

import dev.bluefalcon.BlueFalcon
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import storage.DatabaseFactory
import storage.createDataStore

actual val platformModule: Module
    get() = module {
        single { createDataStore(context = androidContext()) }
//        single { RemoteConfigManager() }
        single { OkHttp.create() }
        single { BlueFalcon(context = androidApplication()) }
        single { DatabaseFactory(androidApplication()) }
    }