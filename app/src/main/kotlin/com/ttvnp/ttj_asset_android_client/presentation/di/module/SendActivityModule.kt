package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SendActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SendActivitySubcomponent::class))
abstract class SendActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(SendActivity::class)
    abstract fun bindSendActivityInjectorFactory(builder: SendActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}