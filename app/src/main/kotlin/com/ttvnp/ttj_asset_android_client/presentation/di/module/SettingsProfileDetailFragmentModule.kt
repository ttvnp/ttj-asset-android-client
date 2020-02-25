package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileDetailFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileDetailFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsProfileDetailFragmentSubcomponent::class])
abstract class SettingsProfileDetailFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsProfileDetailFragment::class)
    abstract fun bindSettingsProfileDetailFragmentInjectorFactory(factory: SettingsProfileDetailFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}