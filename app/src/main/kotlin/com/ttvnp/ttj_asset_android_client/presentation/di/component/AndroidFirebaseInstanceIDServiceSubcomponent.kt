package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ServiceModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.service.AndroidFirebaseInstanceIDService
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        ServiceModule::class,
        DataModule::class,
        DomainModule::class
))
interface AndroidFirebaseInstanceIDServiceSubcomponent : AndroidInjector<AndroidFirebaseInstanceIDService> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<AndroidFirebaseInstanceIDService>() {}
}
