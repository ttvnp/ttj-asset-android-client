package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.*
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SendActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
        ActivityModule::class,
        DataModule::class,
        DomainModule::class,
        SendEmailFormFragmentModule::class,
        SendAmountFormFragmentModule::class,
        SendAmountFormByStellarFragmentModule::class,
        SendAmountConfirmFragmentModule::class
])
interface SendActivitySubcomponent : AndroidInjector<SendActivity> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SendActivity>
}
