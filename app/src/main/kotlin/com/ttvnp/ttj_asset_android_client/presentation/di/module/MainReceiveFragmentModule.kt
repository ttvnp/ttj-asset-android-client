package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.MainReceiveFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainReceiveFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(MainReceiveFragmentSubcomponent::class))
abstract class MainReceiveFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(MainReceiveFragment::class)
    abstract fun bindMainReceiveFragmentInjectorFactory(builder: MainReceiveFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}