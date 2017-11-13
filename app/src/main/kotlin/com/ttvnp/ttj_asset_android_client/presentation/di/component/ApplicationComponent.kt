package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.AndroidApplication
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ApplicationModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.MainActivityModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ReceiveSetAmountActivityModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.TutorialActivityModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        TutorialActivityModule::class,
        MainActivityModule::class,
        ReceiveSetAmountActivityModule::class
))
interface ApplicationComponent {
    fun inject(androidApplication: AndroidApplication)
}
