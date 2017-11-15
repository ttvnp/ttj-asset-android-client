package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.AndroidApplication
import com.ttvnp.ttj_asset_android_client.presentation.di.module.*
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        ApplicationModule::class,
        LaunchActivityModule::class,
        TutorialActivityModule::class,
        MainActivityModule::class,
        ReceiveSetAmountActivityModule::class,
        SendActivityModule::class
))
interface ApplicationComponent {
    fun inject(androidApplication: AndroidApplication)
}
