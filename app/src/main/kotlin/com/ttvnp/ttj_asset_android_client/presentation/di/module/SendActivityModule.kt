package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SendActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SendActivitySubcomponent::class])
abstract class SendActivityModule {
    @Binds
    @IntoMap
    @ClassKey(SendActivity::class)
    abstract fun bindSendActivityInjectorFactory(factory: SendActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}