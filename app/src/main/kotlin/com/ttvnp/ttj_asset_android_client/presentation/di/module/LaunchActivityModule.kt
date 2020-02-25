package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.LaunchActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.LaunchActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [LaunchActivitySubcomponent::class])
abstract class LaunchActivityModule {
    @Binds
    @IntoMap
    @ClassKey(LaunchActivity::class)
    abstract fun bindLaunchActivityInjectorFactory(factory: LaunchActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
