package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.ActivityModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsChangePasswordActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
        ActivityModule::class,
        DataModule::class,
        DomainModule::class
])
interface SettingsChangePasswordActivitySubcomponent : AndroidInjector<SettingsChangePasswordActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SettingsChangePasswordActivity>
}