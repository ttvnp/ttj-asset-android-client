package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceRegisterEmailResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceVerifyEmailResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.LogoutResponse
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface DeviceService {
    @Headers("Accept: application/json")
    @GET("devices")
    fun get(): Call<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("devices/device_token")
    fun updateDeviceToken(@Field("deviceToken") deviceToken: String) : Call<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PATCH("devices/notification_settings")
    fun updateNotificationSettings(
            @Field("grantPushNotification") grantPushNotification: Boolean
    ): Call<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices/email")
    fun registerEmail(@Field("emailAddress") emailAddress: String): Call<DeviceRegisterEmailResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices/verify_email")
    fun verifyEmail(@Field("verificationCode") verificationCode: String,
                    @Field("passwordOnImport") passwordOnImport: String
    ): Call<DeviceVerifyEmailResponse>

    @Headers("Accept: application/json")
    @POST("devices/logout")
    fun logout(): Call<LogoutResponse>

}

class DeviceServiceImpl(
        deviceInfoDataStore: DeviceInfoDataStore,
        deviceDataStore: DeviceDataStore,
        deviceServiceWithNoAuth: DeviceServiceWithNoAuth
) : BaseAuthService(deviceInfoDataStore, deviceDataStore, deviceServiceWithNoAuth), DeviceService {

    private val service: DeviceService

    init {
        val moshi = Moshi.Builder()
                .add(DateAdapter.FACTORY)
                .build()
        val okClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(getRequestInterceptor())
                .addInterceptor(getAccessTokenInterceptor())
                .build()
        val builder = Retrofit.Builder()
                .client(okClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(getBaseURL())
                .build()
        service = builder.create(DeviceService::class.java)
    }

    override fun get(): Call<DeviceResponse> {
        return service.get()
    }

    override fun updateDeviceToken(deviceToken: String): Call<DeviceResponse> {
        return service.updateDeviceToken(deviceToken)
    }

    override fun updateNotificationSettings(grantPushNotification: Boolean): Call<DeviceResponse> {
        return service.updateNotificationSettings(grantPushNotification)
    }

    override fun registerEmail(emailAddress: String): Call<DeviceRegisterEmailResponse> {
        return service.registerEmail(emailAddress)
    }

    override fun verifyEmail(verificationCode: String, passwordOnImport: String): Call<DeviceVerifyEmailResponse> {
        return service.verifyEmail(verificationCode, passwordOnImport)
    }

    override fun logout(): Call<LogoutResponse> {
        return service.logout()
    }
}
