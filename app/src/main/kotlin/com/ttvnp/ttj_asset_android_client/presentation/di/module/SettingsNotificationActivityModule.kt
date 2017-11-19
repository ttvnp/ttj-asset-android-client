package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsNotificationActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsNotificationActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsNotificationActivitySubcomponent::class))
abstract class SettingsNotificationActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SettingsNotificationActivity::class)
    abstract fun bindSettingsNotificationActivityInjectorFactory(builder: SettingsNotificationActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}