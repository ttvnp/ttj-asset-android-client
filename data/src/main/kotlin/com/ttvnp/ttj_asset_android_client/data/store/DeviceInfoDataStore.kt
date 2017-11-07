package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.CryptDriver
import com.ttvnp.ttj_asset_android_client.data.driver.SharedPreferencesDriver
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import javax.inject.Inject

interface DeviceInfoDataStore {
    fun get(): DeviceInfoEntity?
    fun save(entity: DeviceInfoEntity)
}

class DeviceInfoDataStoreImpl @Inject constructor(
        val cryptDriver: CryptDriver,
        val sharedPreferencesDriver: SharedPreferencesDriver
) : DeviceInfoDataStore {

    private val DEVICE_CODE_KEY = "device.device_code"
    private val CREDENTIAL_KEY = "device.credential"

    override fun get(): DeviceInfoEntity? {

        var deviceCode = sharedPreferencesDriver.getString(DEVICE_CODE_KEY)
        var credential = sharedPreferencesDriver.getString(CREDENTIAL_KEY)

        if (deviceCode == null || credential == null) return null

        // decrypt
        val deviceCodePlainText = cryptDriver.decrypt(deviceCode)
        if (deviceCodePlainText == null) return null
        deviceCode = deviceCodePlainText

        val credentialPlainText = cryptDriver.decrypt(credential)
        if (credentialPlainText == null) return null
        credential = credentialPlainText

        return DeviceInfoEntity(deviceCode, credential)
    }

    override fun save(entity: DeviceInfoEntity) {
        val deviceCode = cryptDriver.encrypt(entity.deviceCode)
        val credential = cryptDriver.encrypt(entity.credential)
        sharedPreferencesDriver.putString(DEVICE_CODE_KEY, deviceCode!!)
        sharedPreferencesDriver.putString(CREDENTIAL_KEY, credential!!)
    }
}
