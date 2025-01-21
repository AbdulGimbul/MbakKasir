package di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import features.cashier_role.home.domain.ProductMbakKasirDatabase
import org.koin.dsl.module
import storage.DatabaseFactory
import storage.SessionHandler

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