package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainSettingsFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSettingsFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [(MainSettingsFragmentSubcomponent::class)])
abstract class MainSettingsFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MainSettingsFragment::class)
    abstract fun bindMainSettingsFragmentInjectorFactory(factory: MainSettingsFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}