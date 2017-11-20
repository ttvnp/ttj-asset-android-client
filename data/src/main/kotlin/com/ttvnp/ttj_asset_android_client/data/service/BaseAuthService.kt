package com.ttvnp.ttj_asset_android_client.data.service

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import okhttp3.Interceptor

abstract class BaseAuthService(
        private val deviceInfoDataStore: DeviceInfoDataStore,
        private val deviceDataStore: DeviceDataStore,
        private val deviceServiceWithNoAuth: DeviceServiceWithNoAuth
) : BaseService() {

    companion object {
        val lock = java.lang.Object()
    }

    open protected fun getAccessTokenInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()
            builder.addHeader("Accept", "application/json")
            val setAuthHeader: (String) -> Unit = { token ->
                if (1 < token.length) builder.addHeader("Authorization", "Bearer " + token)
            }
            var accessToken: String = getAccessToken()
            setAuthHeader(accessToken)
            request = builder.build();
            var response = chain.proceed(request)
            if (response.code() == 401) { // if unauthorized
                synchronized(lock) {
                    accessToken = retrieveAccessToken()
                    setAuthHeader(accessToken)
                    request = builder.build();
                    response = chain.proceed(request); // repeat request with new token.
                }
            }
            response
        }
    }

    open protected fun getAccessToken(): String {
        val deviceEntity = deviceDataStore.get()
        if (deviceEntity == null || deviceEntity.accessTokenExpiry == null) return ""
        if (deviceEntity.accessTokenExpiry.before(Now())) {
            return retrieveAccessToken()
        }
        return deviceEntity.accessToken
    }

    open protected fun retrieveAccessToken(): String {
        val deviceEntity = deviceDataStore.get()
        if (deviceEntity == null) return ""
        val deviceInfoEntity = deviceInfoDataStore.get()
        deviceInfoEntity?.let {
            deviceServiceWithNoAuth.issueAccessToken(it.deviceCode,it.credential).execute().body()?.let {
                if (it.hasError()) {
                    return ""
                }
                val newDeviceEntity = DeviceEntity(
                        accessToken = it.accessToken,
                        accessTokenExpiry = it.accessTokenExpiry,
                        deviceToken = deviceEntity.deviceToken,
                        grantPushNotification = deviceEntity.grantPushNotification,
                        grantEmailNotification = deviceEntity.grantEmailNotification
                )
                deviceDataStore.update(newDeviceEntity)
                return newDeviceEntity.accessToken
            }
        }
        return ""
    }
}
