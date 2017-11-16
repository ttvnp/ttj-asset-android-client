package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileDetailFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileDetailFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsProfileDetailFragmentSubcomponent::class))
abstract class SettingsProfileDetailFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(SettingsProfileDetailFragment::class)
    abstract fun bindSettingsProfileDetailFragmentInjectorFactory(builder: SettingsProfileDetailFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}