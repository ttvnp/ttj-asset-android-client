package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainSendFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSendFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [MainSendFragmentSubcomponent::class])
abstract class MainSendFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(MainSendFragment::class)
    abstract fun bindMainSendFragmentInjectorFactory(factory: MainSendFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
