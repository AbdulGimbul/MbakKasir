package dev.mbakasir.com.di

import dev.bluefalcon.BlueFalcon
import dev.mbakasir.com.storage.DatabaseFactory
import dev.mbakasir.com.storage.createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { createDataStore(context = androidContext()) }
//        single { RemoteConfigManager() }
        single { OkHttp.create() }
        single { BlueFalcon(context = androidApplication()) }
        single { DatabaseFactory(androidApplication()) }
    }