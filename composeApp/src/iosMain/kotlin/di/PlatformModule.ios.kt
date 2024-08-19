package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import storage.createDataStore
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

//actual val platformModule: Module
//    get() = module {
//        single { createDataStore() }
//        single { Darwin.create() }
//    }