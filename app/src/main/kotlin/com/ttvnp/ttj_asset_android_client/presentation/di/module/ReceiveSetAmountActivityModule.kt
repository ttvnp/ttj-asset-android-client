package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.ReceiveSetAmountActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.ReceiveSetAmountActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [ReceiveSetAmountActivitySubcomponent::class])
abstract class ReceiveSetAmountActivityModule {
    @Binds
    @IntoMap
    @ClassKey(ReceiveSetAmountActivity::class)
    abstract fun bindReceiveSetAmountActivityInjectorFactory(factory: ReceiveSetAmountActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}