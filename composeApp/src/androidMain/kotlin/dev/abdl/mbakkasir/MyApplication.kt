package dev.abdl.mbakkasir

import android.app.Application

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
//        initKoin {
//            androidContext(this@MyApplication)
//        }
    }
}