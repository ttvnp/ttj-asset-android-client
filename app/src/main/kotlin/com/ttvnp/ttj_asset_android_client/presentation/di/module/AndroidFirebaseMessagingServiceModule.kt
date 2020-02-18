package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.AndroidFirebaseMessagingServiceSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.service.AndroidFirebaseMessagingService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [AndroidFirebaseMessagingServiceSubcomponent::class])
abstract class AndroidFirebaseMessagingServiceModule {
    @Binds
    @IntoMap
    @ClassKey(AndroidFirebaseMessagingService::class)
    abstract fun bindAndroidFirebaseMessagingServiceInjectorFactory(factory: AndroidFirebaseMessagingServiceSubcomponent.Factory): AndroidInjector.Factory<*>
}
