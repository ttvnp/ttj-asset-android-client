package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.ActivityModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsSecurityActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        DataModule::class,
        DomainModule::class
))
interface SettingsSecurityActivitySubcomponent : AndroidInjector<SettingsSecurityActivity> {
    @Subcomponent.Builder
    abstract class Builder: AndroidInjector.Builder<SettingsSecurityActivity>()
}