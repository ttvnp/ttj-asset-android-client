package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.*
import io.reactivex.Single

interface DeviceRepository {

    fun getLanguage(): String

    fun saveLanguage(language: String)

    fun getDevice(): Single<ModelWrapper<DeviceModel?>>

    fun register(): Single<ModelWrapper<DeviceModel?>>

    fun registerEmail(emailAddress: String): Single<ModelWrapper<RegisterEmailResultModel?>>

    fun verifyEmail(verificationCode: String, passwordOnImport: String): Single<ModelWrapper<UserModel?>>

    fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>>

    fun updateNotificationSettings(grantPushNotification: Boolean?): Single<ModelWrapper<DeviceModel?>>

    fun logout(): Single<ModelWrapper<LogoutModel?>>
}
