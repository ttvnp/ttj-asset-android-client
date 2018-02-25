package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.*
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        ServiceModule::class,
        DataModule::class,
        DomainModule::class,
        MainHomeFragmentModule::class,
        MainReceiveFragmentModule::class,
        MainSendFragmentModule::class,
        MainSettingsFragmentModule::class
))
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>() {}
}