package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.DeviceTokenUpdater
import dagger.Module
import dagger.Provides

@Module
class ServiceModule {
    @Provides
    fun deviceTokenUpdater(deviceUseCase: DeviceUseCase): DeviceTokenUpdater {
        return DeviceTokenUpdater(deviceUseCase)
    }
}