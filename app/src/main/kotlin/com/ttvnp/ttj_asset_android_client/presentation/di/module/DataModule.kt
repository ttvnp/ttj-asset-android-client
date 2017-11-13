package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.data.driver.CryptDriver
import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.driver.SharedPreferencesDriver
import com.ttvnp.ttj_asset_android_client.data.service.*
import com.ttvnp.ttj_asset_android_client.data.store.*
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    // DataStores
    @Provides
    fun deviceInfoDataStore(
            cryptDriver: CryptDriver,
            sharedPreferencesDriver: SharedPreferencesDriver
    ): DeviceInfoDataStore = DeviceInfoDataStoreImpl(cryptDriver, sharedPreferencesDriver)

    @Provides
    fun deviceDataStore(ormaHolder: OrmaHolder): DeviceDataStore = DeviceDataStoreImpl(ormaHolder)

    @Provides
    fun userDataStore(ormaHolder: OrmaHolder): UserDataStore = UserDataStoreImpl(ormaHolder)

    @Provides
    fun balanceDataStore(ormaHolder: OrmaHolder): BalanceDataStore = BalanceDataStoreImpl(ormaHolder)

    @Provides
    fun userTransactionDataStore(ormaHolder: OrmaHolder): UserTransactionDataStore = UserTransactionDataStoreImpl(ormaHolder)

    // Services
    @Provides
    fun deviceServiceWithNoAuth(): DeviceServiceWithNoAuth = DeviceServiceWithNoAuthImpl()

    @Provides
    fun deviceService(
            deviceInfoDataStore: DeviceInfoDataStore,
            deviceDataStore: DeviceDataStore,
            deviceServiceWithNoAuth: DeviceServiceWithNoAuth
    ): DeviceService = DeviceServiceImpl(deviceInfoDataStore, deviceDataStore, deviceServiceWithNoAuth)

    @Provides
    fun userService(
            deviceInfoDataStore: DeviceInfoDataStore,
            deviceDataStore: DeviceDataStore,
            deviceServiceWithNoAuth: DeviceServiceWithNoAuth
    ): UserService = UserServiceImpl(deviceInfoDataStore, deviceDataStore, deviceServiceWithNoAuth)

}
