package dev.mbakasir.com

import android.app.Application
import android.content.Context
import dev.mbakasir.com.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
        appContext = applicationContext
    }

    companion object {
        lateinit var appContext: Context
            private set
    }
}