package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.CryptDriver
import com.ttvnp.ttj_asset_android_client.data.driver.SharedPreferencesDriver
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import javax.inject.Inject

const val KEY_LANGUAGE = "language"

interface DeviceInfoDataStore {
    fun getLanguage(): String
    fun saveLanguage(language: String)
    fun get(): DeviceInfoEntity?
    fun save(entity: DeviceInfoEntity)
    fun removeDeviceInfo()
}

class DeviceInfoDataStoreImpl @Inject constructor(
        val cryptDriver: CryptDriver,
        val sharedPreferencesDriver: SharedPreferencesDriver
) : DeviceInfoDataStore {

    private val DEVICE_CODE_KEY = "device.device_code"
    private val CREDENTIAL_KEY = "device.credential"

    private var cached: DeviceInfoEntity? = null

    override fun getLanguage(): String {
        return sharedPreferencesDriver.getString(KEY_LANGUAGE)?: "en"
    }

    override fun saveLanguage(language: String) {
        sharedPreferencesDriver.putString(KEY_LANGUAGE, language)
    }

    override fun get(): DeviceInfoEntity? {

        if (cached != null) return cached

        var deviceCode = sharedPreferencesDriver.getString(DEVICE_CODE_KEY)
        var credential = sharedPreferencesDriver.getString(CREDENTIAL_KEY)

        if (deviceCode.isNullOrBlank() || credential.isNullOrBlank()) return null

        // decrypt
        val deviceCodePlainText = cryptDriver.decrypt(deviceCode!!) ?: return null
        deviceCode = deviceCodePlainText

        val credentialPlainText = cryptDriver.decrypt(credential!!) ?: return null
        credential = credentialPlainText

        cached = DeviceInfoEntity(deviceCode, credential)
        return cached
    }

    override fun save(entity: DeviceInfoEntity) {
        val deviceCode = cryptDriver.encrypt(entity.deviceCode)
        val credential = cryptDriver.encrypt(entity.credential)
        sharedPreferencesDriver.putString(DEVICE_CODE_KEY, deviceCode!!)
        sharedPreferencesDriver.putString(CREDENTIAL_KEY, credential!!)
        cached = entity
    }

    override fun removeDeviceInfo() {
        cached = null
        sharedPreferencesDriver.removeString(DEVICE_CODE_KEY)
        sharedPreferencesDriver.removeString(CREDENTIAL_KEY)
    }
}
