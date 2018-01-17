package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainSendFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSendFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MainSendFragmentSubcomponent::class))
abstract class MainSendFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MainSendFragment::class)
    abstract fun bindMainSendFragmentInjectorFactory(builder: MainSendFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}