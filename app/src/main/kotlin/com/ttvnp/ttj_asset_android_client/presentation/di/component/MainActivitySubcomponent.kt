package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.ActivityModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.MainHomeFragmentModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.MainActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        DataModule::class,
        DomainModule::class,
        MainHomeFragmentModule::class
))
interface MainActivitySubcomponent : AndroidInjector<MainActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<MainActivity>() {}
}
