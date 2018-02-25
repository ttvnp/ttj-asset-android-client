package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainSettingsFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSettingsFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [(MainSettingsFragmentSubcomponent::class)])
abstract class MainSettingsFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MainSettingsFragment::class)
    abstract fun bindMainSettingsFragmentInjectorFactory(builder: MainSettingsFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}