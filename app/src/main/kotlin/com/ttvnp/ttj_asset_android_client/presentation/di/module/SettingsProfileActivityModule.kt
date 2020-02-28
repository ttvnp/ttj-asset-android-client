package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsProfileActivitySubcomponent::class])
abstract class SettingsProfileActivityModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsProfileActivity::class)
    abstract fun bindSettingsProfileActivityInjectorFactory(factory: SettingsProfileActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
