package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceRegisterEmailResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceVerifyEmailResponse
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface DeviceService {
    @Headers("Accept: application/json")
    @GET("devices")
    fun get() : Single<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PUT("devices/device_token")
    fun updateDeviceToken(@Field("deviceToken") deviceToken: String) : Single<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PATCH("devices/notification_settings")
    fun updateNotificationSettings(
            @Field("grantPushNotification") grantPushNotification: Boolean,
            @Field("grantEmailNotification") grantEmailNotification: Boolean
    ) : Single<DeviceResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices/email")
    fun registerEmail(@Field("emailAddress") emailAddress: String) : Single<DeviceRegisterEmailResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("devices/verify_email")
    fun verifyEmail(@Field("verificationCode") verificationCode: String) : Single<DeviceVerifyEmailResponse>
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

    override fun get(): Single<DeviceResponse> {
        return service.get()
    }

    override fun updateDeviceToken(deviceToken: String): Single<DeviceResponse> {
        return service.updateDeviceToken(deviceToken)
    }

    override fun updateNotificationSettings(grantPushNotification: Boolean, grantEmailNotification: Boolean): Single<DeviceResponse> {
        return service.updateNotificationSettings(grantPushNotification, grantEmailNotification)
    }

    override fun registerEmail(emailAddress: String): Single<DeviceRegisterEmailResponse> {
        return service.registerEmail(emailAddress)
    }

    override fun verifyEmail(verificationCode: String): Single<DeviceVerifyEmailResponse> {
        return service.verifyEmail(verificationCode)
    }
}
