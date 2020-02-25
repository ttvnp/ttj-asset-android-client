package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsNotificationActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsNotificationActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsNotificationActivitySubcomponent::class])
abstract class SettingsNotificationActivityModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsNotificationActivity::class)
    abstract fun bindSettingsNotificationActivityInjectorFactory(factory: SettingsNotificationActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}