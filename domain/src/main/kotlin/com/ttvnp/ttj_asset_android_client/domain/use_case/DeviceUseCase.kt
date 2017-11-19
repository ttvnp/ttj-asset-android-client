package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.isEmailValid
import io.reactivex.Single
import javax.inject.Inject

interface DeviceUseCase {

    fun getDevice(): Single<ModelWrapper<DeviceModel?>>

    fun init() : Single<ModelWrapper<DeviceModel?>>

    fun registerEmail(emailAddress : String) : Single<DeviceModel>

    fun verifyEmail(verificationCode : String) : Single<UserModel>

    fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>>

    fun updateGrantPushNotification(grantPushNotification: Boolean): Single<ModelWrapper<DeviceModel?>>

    fun updateGrantEmailNotification(grantEmailNotification: Boolean): Single<ModelWrapper<DeviceModel?>>
}

class DeviceUseCaseImpl @Inject constructor(
        private val repository: DeviceRepository
) : DeviceUseCase {

    override fun getDevice(): Single<ModelWrapper<DeviceModel?>> {
        return repository.getDevice()
    }

    override fun init(): Single<ModelWrapper<DeviceModel?>> {
        return repository.register()
    }

    override fun registerEmail(emailAddress: String): Single<DeviceModel> {
        val input = emailAddress.trim()
        if (!isEmailValid(input)) {
            throw ValidationException("emailAddress")
        }
        return repository.registerEmail(input)
    }

    override fun verifyEmail(verificationCode: String): Single<UserModel> {
        val input = verificationCode.trim()
        return repository.verifyEmail(input)
    }

    override fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>> {
        return repository.updateDeviceToken(deviceToken)
    }

    override fun updateGrantPushNotification(grantPushNotification: Boolean): Single<ModelWrapper<DeviceModel?>> {
        return repository.updateNotificationSettings(grantPushNotification, null)
    }

    override fun updateGrantEmailNotification(grantEmailNotification: Boolean): Single<ModelWrapper<DeviceModel?>> {
        return repository.updateNotificationSettings(null, grantEmailNotification)
    }
}

