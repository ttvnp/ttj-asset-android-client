package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainReceiveFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainReceiveFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainReceiveFragmentSubcomponent::class])
abstract class MainReceiveFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MainReceiveFragment::class)
    abstract fun bindMainReceiveFragmentInjectorFactory(factory: MainReceiveFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
