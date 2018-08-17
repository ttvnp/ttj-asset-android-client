package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsChangePasswordActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsChangePasswordActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsChangePasswordActivitySubcomponent::class))
abstract class SettingsChangePasswordActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SettingsChangePasswordActivity::class)
    abstract fun bindSettingsChangePasswordActivityInjectorFactory(
            builder: SettingsChangePasswordActivitySubcomponent.Builder
    ): AndroidInjector.Factory<out Activity>
}