package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.service.DeviceServiceWithNoAuth
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.DeviceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.data.util.TokenUtil
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import io.reactivex.Single
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceServiceWithNoAuth : DeviceServiceWithNoAuth,
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore,
        private val userDataStore : UserDataStore
) : DeviceRepository {

    override fun getDevice(): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            val deviceInfo = deviceInfoDataStore.get()
            if (deviceInfo == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                return@create
            }

            // get from device info
            var deviceEntity: DeviceEntity? = null
            deviceEntity = deviceDataStore.get() // at first from local.
            if (deviceEntity == null) {
                deviceServiceWithNoAuth.issueAccessToken(deviceInfo.deviceCode, deviceInfo.credential).execute().body()?.let { response ->
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                        return@create
                    }
                    deviceEntity = DeviceEntity(
                            accessToken = response.accessToken,
                            accessTokenExpiry = response.accessTokenExpiry,
                            isActivated = response.isActivated,
                            deviceToken = response.deviceToken,
                            grantPushNotification = response.grantPushNotification,
                            grantEmailNotification = response.grantEmailNotification
                    )
                    deviceEntity = deviceDataStore.update(deviceEntity!!)
                }
            }
            subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
        }
    }

    override fun register(): Single<DeviceModel> {
        // call api service to register this device and retrieve access token as well.
        val deviceInfo = deviceInfoDataStore.get()
        if (deviceInfo != null) {
            val deviceEntity = deviceDataStore.get()
            if (deviceEntity != null) {
                return Single.just(DeviceTranslator().translate(deviceEntity))
            }
        }
        // newly create device code and credential.
        val initDeviceCode = TokenUtil.generateToken68(64)
        val initCredential = TokenUtil.generateToken68(64)

        return deviceServiceWithNoAuth.register(initDeviceCode, initCredential).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            deviceInfoDataStore.save(DeviceInfoEntity(initDeviceCode, initCredential))
            var deviceEntity = DeviceEntity(
                    accessToken = it.accessToken,
                    accessTokenExpiry = it.accessTokenExpiry,
                    isActivated = it.isActivated,
                    deviceToken = it.deviceToken,
                    grantPushNotification = it.grantPushNotification,
                    grantEmailNotification = it.grantEmailNotification
            )
            deviceEntity = deviceDataStore.update(deviceEntity)
            DeviceTranslator().translate(deviceEntity)!!
        }
    }

    override fun registerEmail(emailAddress: String): Single<DeviceModel> {
        return deviceService.registerEmail(emailAddress).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            val deviceEntity = deviceDataStore.get()
            DeviceTranslator().translate(deviceEntity)!!
        }
    }

    override fun verifyEmail(verificationCode: String): Single<UserModel> {
        return deviceService.verifyEmail(verificationCode).map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
            var userEntity = UserEntity(
                    emailAddress = response.emailAddress,
                    profileImageID = response.profileImageID,
                    profileImageURL = response.profileImageURL,
                    firstName = response.firstName,
                    middleName =  response.middleName,
                    lastName = response.lastName,
                    address = response.address,
                    isEmailVerified = response.isEmailVerified,
                    isIdentified = response.isIdentified
            )
            userEntity = userDataStore.update(userEntity)
            UserTranslator().translate(userEntity)!!
        }
    }

}