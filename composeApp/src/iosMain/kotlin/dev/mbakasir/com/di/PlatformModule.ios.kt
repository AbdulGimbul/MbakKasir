package dev.mbakasir.com.di

import dev.bluefalcon.BlueFalcon
import dev.mbakasir.com.storage.DatabaseFactory
import dev.mbakasir.com.storage.createDataStore
import dev.mbakasir.com.utils.ShareManager
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIApplication

actual val platformModule: Module
    get() = module {
        single { createDataStore() }
//        single { RemoteConfigManager() }
        single { Darwin.create() }
        single { BlueFalcon(context = UIApplication.sharedApplication) }
        single { DatabaseFactory() }
        single { ShareManager() }
    }