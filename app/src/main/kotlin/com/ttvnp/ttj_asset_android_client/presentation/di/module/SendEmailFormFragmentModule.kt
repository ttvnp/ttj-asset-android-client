package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendEmailFormFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendEmailFormFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SendEmailFormFragmentSubcomponent::class])
abstract class SendEmailFormFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SendEmailFormFragment::class)
    abstract fun bindSendEmailFormFragmentInjectorFactory(factory: SendEmailFormFragmentSubcomponent.Factory): AndroidInjector.Factory<*>
}
