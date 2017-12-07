package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ttvnp.ttj_asset_android_client.data.crypto.CipherAlgorithm
import com.ttvnp.ttj_asset_android_client.data.crypto.Cryptor
import com.ttvnp.ttj_asset_android_client.data.crypto.CryptorFactory
import com.ttvnp.ttj_asset_android_client.data.driver.*
import com.ttvnp.ttj_asset_android_client.data.store.AppDataStore
import com.ttvnp.ttj_asset_android_client.data.store.AppDataStoreImpl
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStoreImpl
import com.ttvnp.ttj_asset_android_client.presentation.ui.error.ErrorMessageGenerator
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Singleton
    @Provides
    fun applicationContext(): Context = application

    @Provides
    @Singleton
    fun sharedPreferences(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun sharedPreferencesDriver(sharedPreferences: SharedPreferences): SharedPreferencesDriver
            = SharedPreferencesDriverImpl(sharedPreferences)

    @Provides
    @Singleton
    fun ormaHolder(context: Context): OrmaHolder = OrmaHolder(context)

    @Provides
    @Singleton
    fun safetyNetClient(context: Context): SafetyNetClient = SafetyNetClient(context)

    @Provides
    @Singleton
    fun cryptor(context: Context): Cryptor {
        val factory = CryptorFactory("ttj_asset_android_client_secure_data", CipherAlgorithm.RSA)
        factory.context = context
        return factory.createInstance()
    }

    @Provides
    @Singleton
    fun cryptDriver(cryptor: Cryptor): CryptDriver = CryptDriverImpl(cryptor)

    @Provides
    @Singleton
    fun deviceInfoDataStore(
            cryptDriver: CryptDriver,
            sharedPreferencesDriver: SharedPreferencesDriver
    ): DeviceInfoDataStore = DeviceInfoDataStoreImpl(cryptDriver, sharedPreferencesDriver)

    @Provides
    @Singleton
    fun appDataStore(ormaHolder: OrmaHolder): AppDataStore = AppDataStoreImpl(ormaHolder)

    @Provides
    @Singleton
    fun errorMessageGenerator(context: Context): ErrorMessageGenerator
        = ErrorMessageGenerator(context)
}
