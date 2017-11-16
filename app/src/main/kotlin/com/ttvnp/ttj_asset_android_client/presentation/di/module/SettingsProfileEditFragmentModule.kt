package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileEditFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileEditFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsProfileEditFragmentSubcomponent::class))
abstract class SettingsProfileEditFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(SettingsProfileEditFragment::class)
    abstract fun bindSettingsProfileEditFragmentInjectorFactory(builder: SettingsProfileEditFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}