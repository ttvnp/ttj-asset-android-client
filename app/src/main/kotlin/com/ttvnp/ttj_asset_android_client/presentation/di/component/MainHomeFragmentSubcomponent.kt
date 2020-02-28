package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.DataModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.DomainModule
import com.ttvnp.ttj_asset_android_client.presentation.di.module.FragmentModule
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainHomeFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = [
        FragmentModule::class,
        DataModule::class,
        DomainModule::class
])
interface MainHomeFragmentSubcomponent : AndroidInjector<MainHomeFragment> {
    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainHomeFragment>
}