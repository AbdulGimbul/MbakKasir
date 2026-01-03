package dev.mbakasir.com.utils

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient

object ChuckerWrapper {
    fun install(builder: OkHttpClient.Builder, context: Context) {
        builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
    }
}
