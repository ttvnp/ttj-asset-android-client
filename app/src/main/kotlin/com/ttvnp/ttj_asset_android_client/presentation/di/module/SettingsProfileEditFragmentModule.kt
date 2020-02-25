package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileEditFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileEditFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsProfileEditFragmentSubcomponent::class])
abstract class SettingsProfileEditFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsProfileEditFragment::class)
    abstract fun bindSettingsProfileEditFragmentInjectorFactory(factory: SettingsProfileEditFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}