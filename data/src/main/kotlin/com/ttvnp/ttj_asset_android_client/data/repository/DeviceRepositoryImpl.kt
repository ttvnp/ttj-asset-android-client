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
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import io.reactivex.Observable
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceServiceWithNoAuth : DeviceServiceWithNoAuth,
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore,
        private val userDataStore : UserDataStore
) : DeviceRepository {

    override fun register(): Observable<DeviceModel> {
        // call api service to register this device and retrieve access token as well.
        val deviceInfo = deviceInfoDataStore.get()
        if (deviceInfo != null) {
            val deviceEntity = deviceDataStore.get()
            if (deviceEntity != null) {
                return Observable.just(DeviceTranslator().translate(deviceEntity))
            }
        }
        // newly create device code and credential.
        val initDeviceCode = TokenUtil.generateToken68(64)
        val initCredential = TokenUtil.generateToken68(64)

        // TODO handle api error
        return deviceServiceWithNoAuth.register(initDeviceCode, initCredential).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            deviceInfoDataStore.save(DeviceInfoEntity(initDeviceCode, initCredential))
            var deviceEntity = DeviceEntity(
                    accessToken = it.accessToken,
                    accessTokenExpiry = it.accessTokenExpiry,
                    deviceToken = "",
                    grantPushNotification = false,
                    grantEmailNotification = false
            )
            deviceEntity = deviceDataStore.update(deviceEntity)
            DeviceTranslator().translate(deviceEntity)!!
        }
    }

    override fun registerEmail(emailAddress: String): Observable<DeviceModel> {
        return deviceService.registerEmail(emailAddress).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            val deviceEntity = deviceDataStore.get()
            DeviceTranslator().translate(deviceEntity)!!
        }
    }

    override fun verifyEmail(verificationCode: String): Observable<UserModel> {
        return deviceService.verifyEmail(verificationCode).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            var userEntity = UserEntity()
            userEntity = userDataStore.update(userEntity)
            UserTranslator().translate(userEntity)!!
        }
    }

}