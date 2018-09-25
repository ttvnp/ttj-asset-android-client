package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendAmountFormByStellarFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormByStellarFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [(SendAmountFormByStellarFragmentSubcomponent::class)])
abstract class SendAmountFormByStellarFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(SendAmountFormByStellarFragment::class)
    abstract fun bindSendAmountFormByStellarFragmentInjectorFactory(builder: SendAmountFormByStellarFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}