package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.data.repository.DeviceRepositoryImpl
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    // use cases
    @Provides
    fun deviceUseCase(deviceRepository: DeviceRepository): DeviceUseCase {
        return DeviceUseCaseImpl(deviceRepository)
    }

    // repository
    @Provides
    fun deviceRepository(
            deviceService : DeviceService,
            deviceDataStore : DeviceDataStore,
            deviceInfoDataStore : DeviceInfoDataStore
    ): DeviceRepository {
        return DeviceRepositoryImpl(deviceService, deviceDataStore, deviceInfoDataStore)
    }

}