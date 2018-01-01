package com.ttvnp.ttj_asset_android_client.data.service

import com.ttvnp.ttj_asset_android_client.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

abstract class BaseService {

    open protected fun getBaseURL(): String {

        return BuildConfig.SERVER_URL + "/api/v1/"
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
                            .addHeader("User-Agent", "TTJAssetClient (Android)")
                            .build()
            )
        }
    }
}

