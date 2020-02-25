package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendAmountFormByStellarFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormByStellarFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SendAmountFormByStellarFragmentSubcomponent::class])
abstract class SendAmountFormByStellarFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SendAmountFormByStellarFragment::class)
    abstract fun bindSendAmountFormByStellarFragmentInjectorFactory(factory: SendAmountFormByStellarFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
