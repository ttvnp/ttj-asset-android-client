package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainHomeFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainHomeFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainHomeFragmentSubcomponent::class])
abstract class MainHomeFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MainHomeFragment::class)
    abstract fun bindMainHomeFragmentInjectorFactory(factory: MainHomeFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
