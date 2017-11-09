package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceRegisterResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface DeviceServiceWithNoAuth {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices")
    fun register(@Field("deviceCode") deviceCode: String, @Field("credential") credential: String) : Single<DeviceRegisterResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices/access_token")
    fun issueAccessToken(@Field("deviceCode") deviceCode: String, @Field("credential") credential: String) : Call<DeviceRegisterResponse>
}

class DeviceServiceWithNoAuthImpl : BaseService(), DeviceServiceWithNoAuth {

    private val service: DeviceServiceWithNoAuth

    init {
        val moshi = Moshi.Builder()
                .add(DateAdapter.FACTORY)
                .build()
        val okClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(getRequestInterceptor())
                .build()
        val builder = Retrofit.Builder()
                .client(okClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(getBaseURL())
                .build()
        service = builder.create(DeviceServiceWithNoAuth::class.java)
    }

    override fun register(deviceCode: String, credential: String): Single<DeviceRegisterResponse> {
        return service.register(deviceCode, credential)
    }

    override fun issueAccessToken(deviceCode: String, credential: String): Call<DeviceRegisterResponse> {
        return service.issueAccessToken(deviceCode, credential)
    }
}