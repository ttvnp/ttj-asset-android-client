package com.ttvnp.ttj_asset_android_client.data.service

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

abstract class BaseService {

    open protected fun getBaseURL(): String {
        // TODO from build settings
        return "http://192.168.11.9:1324/api/v1/"
        // return "http://10.0.2.2:1324/api/v1/"
    }

    open protected fun getLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    open protected fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.proceed(
                    chain.request()
                            .newBuilder()
                            .addHeader("User-Agent", "VNJCoinClient (Android)")
                            .build()
            )
        }
    }
}

