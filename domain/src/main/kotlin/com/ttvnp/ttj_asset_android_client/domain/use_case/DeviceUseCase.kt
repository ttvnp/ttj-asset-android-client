package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.isEmailValid
import io.reactivex.Single
import javax.inject.Inject

interface DeviceUseCase {

    fun getLanguage(): String

    fun saveLanguage(language: String)

    fun getDevice(): Single<ModelWrapper<DeviceModel?>>

    fun init() : Single<ModelWrapper<DeviceModel?>>

    fun registerEmail(emailAddress : String) : Single<ModelWrapper<RegisterEmailResultModel?>>

    fun verifyEmail(verificationCode : String, passwordOnImport: String) : Single<ModelWrapper<UserModel?>>

    fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>>

    fun updateGrantPushNotification(grantPushNotification: Boolean): Single<ModelWrapper<DeviceModel?>>

    fun logout(): Single<ModelWrapper<LogoutModel?>>

}

class DeviceUseCaseImpl @Inject constructor(
        private val repository: DeviceRepository
) : DeviceUseCase {

    override fun getLanguage(): String {
        return repository.getLanguage()
    }

    override fun saveLanguage(language: String) {
        repository.saveLanguage(language)
    }

    override fun getDevice(): Single<ModelWrapper<DeviceModel?>> {
        return repository.getDevice()
    }

    override fun init(): Single<ModelWrapper<DeviceModel?>> {
        return repository.register()
    }

    override fun registerEmail(emailAddress: String): Single<ModelWrapper<RegisterEmailResultModel?>> {
        val input = emailAddress.trim()
        if (!isEmailValid(input)) {
            return Single.create { subscriber ->
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_VALIDATION_EMAIL))
            }
        }
        return repository.registerEmail(input)
    }

    override fun verifyEmail(verificationCode: String, passwordOnImport: String): Single<ModelWrapper<UserModel?>> {
        return repository.verifyEmail(verificationCode.trim(), passwordOnImport.trim())
    }

    override fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>> {
        return repository.updateDeviceToken(deviceToken)
    }

    override fun updateGrantPushNotification(grantPushNotification: Boolean): Single<ModelWrapper<DeviceModel?>> {
        return repository.updateNotificationSettings(grantPushNotification)
    }

    override fun logout(): Single<ModelWrapper<LogoutModel?>> {
        return repository.logout()
    }
}

