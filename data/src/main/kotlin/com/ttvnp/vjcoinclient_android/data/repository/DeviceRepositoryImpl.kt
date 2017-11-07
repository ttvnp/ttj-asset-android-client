package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.DeviceTranslator
import com.ttvnp.ttj_asset_android_client.data.util.TokenUtil
import com.ttvnp.ttj_asset_android_client.domain.exceptions.DeviceRegisterFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore
) : DeviceRepository {

    override fun register(): DeviceModel? {

        // call api service to register this device and retrieve access token as well.
        var maxRetry = 5
        while (0 < maxRetry) {
            val initDeviceCode = TokenUtil.generateToken68(64)
            val initCredential = TokenUtil.generateToken68(64)
            val response = deviceService.register(initDeviceCode, initCredential).execute()
            response?.body()?.let {
                if (!it.hasError()) {
                    deviceInfoDataStore.save(DeviceInfoEntity(initDeviceCode, initCredential))
                    var deviceEntity = DeviceEntity(
                            accessToken = it.accessToken,
                            accessTokenExpiry = it.accessTokenExpiry,
                            deviceToken = "",
                            grantPushNotification = false,
                            grantEmailNotification = false
                    )
                    deviceEntity = deviceDataStore.update(deviceEntity)
                    return DeviceTranslator().translate(deviceEntity)
                }
            }
            maxRetry--
        }
        throw DeviceRegisterFailedException()
    }

    override fun get(): DeviceModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(model: DeviceModel): DeviceModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}