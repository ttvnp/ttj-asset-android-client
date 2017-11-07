package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.data.driver.CryptDriver
import com.ttvnp.ttj_asset_android_client.data.driver.RealmDriver
import com.ttvnp.ttj_asset_android_client.data.driver.SharedPreferencesDriver
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.service.DeviceServiceImpl
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStoreImpl
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStoreImpl
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    // Services
    @Provides
    fun deviceService(): DeviceService = DeviceServiceImpl()

    // DataStores
    @Provides
    fun deviceDataStore(realmDriver: RealmDriver): DeviceDataStore = DeviceDataStoreImpl(realmDriver)

    @Provides
    fun deviceInfoDataStore(
            cryptDriver: CryptDriver,
            sharedPreferencesDriver: SharedPreferencesDriver
    ): DeviceInfoDataStore = DeviceInfoDataStoreImpl(cryptDriver, sharedPreferencesDriver)
}
