package com.ttvnp.ttj_asset_android_client.data.service

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

abstract class BaseService {

    open protected fun getBaseURL(): String {
        // TODO from settings
        return "http://10.0.2.2:1324/api/v1/"
    }

    open protected fun getLogginInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    open protected fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            // TODO retrieve token here
            val token : String = "sample token string"
            chain.proceed(
                    chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .addHeader("User-Agent", "VNJCoinClient (Android)")
                            .build()
            )}
    }
}
