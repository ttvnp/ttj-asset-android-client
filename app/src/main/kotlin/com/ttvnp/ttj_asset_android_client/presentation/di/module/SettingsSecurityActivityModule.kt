package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsSecurityActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsSecurityActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents =  arrayOf(SettingsSecurityActivitySubcomponent::class))
abstract class SettingsSecurityActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SettingsSecurityActivity::class)
    abstract fun bindSettingsSecurityActivityInjectFactory(
            builder: SettingsSecurityActivitySubcomponent.Builder
    ): AndroidInjector.Factory<out Activity>
}