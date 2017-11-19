package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Service
import com.ttvnp.ttj_asset_android_client.presentation.di.component.AndroidFirebaseInstanceIDServiceSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.service.AndroidFirebaseInstanceIDService
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.ServiceKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(AndroidFirebaseInstanceIDServiceSubcomponent::class))
abstract class AndroidFirebaseInstanceIDServiceModule {
    @Binds
    @IntoMap
    @ServiceKey(AndroidFirebaseInstanceIDService::class)
    abstract fun bindAndroidFirebaseInstanceIDServiceInjectorFactory(builder: AndroidFirebaseInstanceIDServiceSubcomponent.Builder): AndroidInjector.Factory<out Service>
}