package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.ReceiveSetAmountActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.ReceiveSetAmountActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(ReceiveSetAmountActivitySubcomponent::class))
abstract class ReceiveSetAmountActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(ReceiveSetAmountActivity::class)
    abstract fun bindReceiveSetAmountActivityInjectorFactory(builder: ReceiveSetAmountActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}