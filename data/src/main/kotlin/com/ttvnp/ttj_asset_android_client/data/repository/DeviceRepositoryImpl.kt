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
import io.reactivex.Observable
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore
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
        return deviceService.register(initDeviceCode, initCredential).map {
            if (it.hasError()) {
                throw DeviceRegisterFailedException()
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

    override fun get(): Observable<DeviceModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(model: DeviceModel): Observable<DeviceModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}