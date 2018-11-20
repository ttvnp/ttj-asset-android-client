package com.ttvnp.ttj_asset_android_client.data.service

import com.ttvnp.ttj_asset_android_client.data.BuildConfig
import com.ttvnp.ttj_asset_android_client.data.service.response.StellarAccountResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StellarService {
    @GET("/accounts/{accountId}")
    fun getSingleAccount(@Path("accountId") accountIdD: String): Call<StellarAccountResponse>

    fun removeDecimal(decimalNumber: String): Long
}

class StellarServiceImpl : StellarService {

    private val service: StellarService

    init {
        val builder = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BuildConfig.HORIZON_URL)
                .build()
        service = builder.create(StellarService::class.java)
    }

    override fun getSingleAccount(accountIdD: String): Call<StellarAccountResponse> {
        return service.getSingleAccount(accountIdD)
    }

    override fun removeDecimal(decimalNumber: String): Long {
        val num = decimalNumber.replace(".", "")
        return num.toLong()
    }
}