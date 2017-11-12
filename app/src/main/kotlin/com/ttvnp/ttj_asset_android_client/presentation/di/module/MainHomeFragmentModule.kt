package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainHomeFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainHomeFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MainHomeFragmentSubcomponent::class))
abstract class MainHomeFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MainHomeFragment::class)
    abstract fun bindMainHomeFragmentInjectorFactory(builder: MainHomeFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}
