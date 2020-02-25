package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendAmountConfirmFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountConfirmFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SendAmountConfirmFragmentSubcomponent::class])
abstract class SendAmountConfirmFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SendAmountConfirmFragment::class)
    abstract fun bindSendAmountConfirmFragmentInjectorFactory(factory: SendAmountConfirmFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}