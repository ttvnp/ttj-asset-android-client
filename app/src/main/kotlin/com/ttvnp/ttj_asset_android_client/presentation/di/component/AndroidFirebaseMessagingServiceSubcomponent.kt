package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ServiceModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.service.AndroidFirebaseMessagingService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
        ServiceModule::class,
        DataModule::class,
        DomainModule::class
])
interface AndroidFirebaseMessagingServiceSubcomponent : AndroidInjector<AndroidFirebaseMessagingService> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<AndroidFirebaseMessagingService> {}
}
