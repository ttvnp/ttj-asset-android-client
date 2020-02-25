package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.FragmentModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormByStellarFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [(FragmentModule::class), (DataModule::class), (DomainModule::class)])
interface SendAmountFormByStellarFragmentSubcomponent : AndroidInjector<SendAmountFormByStellarFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<SendAmountFormByStellarFragment>
}
