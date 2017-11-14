package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.FragmentModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendEmailFormFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        FragmentModule::class,
        DataModule::class,
        DomainModule::class
))
interface SendEmailFormFragmentSubcomponent : AndroidInjector<SendEmailFormFragment> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SendEmailFormFragment>() {}
}