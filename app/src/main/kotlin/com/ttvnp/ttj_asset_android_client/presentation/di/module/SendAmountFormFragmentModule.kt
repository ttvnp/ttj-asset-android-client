package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendAmountFormFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SendAmountFormFragmentSubcomponent::class])
abstract class SendAmountFormFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SendAmountFormFragment::class)
    abstract fun bindSendAmountFormFragmentInjectorFactory(factory: SendAmountFormFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
