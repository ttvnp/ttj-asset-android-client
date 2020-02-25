package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsSecurityActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsSecurityActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsSecurityActivitySubcomponent::class])
abstract class SettingsSecurityActivityModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsSecurityActivity::class)
    abstract fun bindSettingsSecurityActivityInjectFactory(factory: SettingsSecurityActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
