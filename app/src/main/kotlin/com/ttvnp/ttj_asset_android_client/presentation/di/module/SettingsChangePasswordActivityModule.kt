package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsChangePasswordActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsChangePasswordActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsChangePasswordActivitySubcomponent::class])
abstract class SettingsChangePasswordActivityModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsChangePasswordActivity::class)
    abstract fun bindSettingsChangePasswordActivityInjectorFactory(factory: SettingsChangePasswordActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
