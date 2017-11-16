package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsProfileActivitySubcomponent::class))
abstract class SettingsProfileActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SettingsProfileActivity::class)
    abstract fun bindSettingsProfileActivityInjectorFactory(builder: SettingsProfileActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}