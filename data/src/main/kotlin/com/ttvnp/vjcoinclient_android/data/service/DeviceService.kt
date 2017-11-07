package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceRegisterResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface DeviceService {

    @Headers(
            "Accept: application/json"
    )
    @FormUrlEncoded
    @POST("devices")
    fun register(@Field("deviceCode") deviceCode: String, @Field("credential") credential: String) : Call<DeviceRegisterResponse>

}

class DeviceServiceImpl : BaseService(), DeviceService {

    private val service: DeviceService

    init {
        val moshi = Moshi.Builder()
                .add(DateAdapter.FACTORY)
                .build()
        val okClient = OkHttpClient.Builder()
                .addInterceptor(getLogginInterceptor())
                .addInterceptor(getRequestInterceptor())
                .build()
        val builder = Retrofit.Builder()
                .client(okClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(getBaseURL())
                .build()
        service = builder.create(DeviceService::class.java)
    }

    override fun register(deviceCode: String, credential: String): Call<DeviceRegisterResponse> {
        return service.register(deviceCode, credential)
    }
}