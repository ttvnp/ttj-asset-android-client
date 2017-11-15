package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.LaunchActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.LaunchActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(LaunchActivitySubcomponent::class))
abstract class LaunchActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(LaunchActivity::class)
    abstract fun bindLaunchActivityInjectorFactory(builder: LaunchActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}