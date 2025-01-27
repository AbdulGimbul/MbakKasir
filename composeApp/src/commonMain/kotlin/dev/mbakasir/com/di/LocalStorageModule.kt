package dev.mbakasir.com.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.mbakasir.com.storage.DatabaseFactory
import dev.mbakasir.com.storage.ProductMbakKasirDatabase
import dev.mbakasir.com.storage.SessionHandler
import org.koin.dsl.module

val provideLocalStorageModule = module {
    single { SessionHandler(get()) }

    single {
        get<DatabaseFactory>().create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<ProductMbakKasirDatabase>().productDao }
    single { get<ProductMbakKasirDatabase>().productTransDraftDao }
}