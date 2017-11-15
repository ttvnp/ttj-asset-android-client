package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SendEmailFormFragmentSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendEmailFormFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SendEmailFormFragmentSubcomponent::class))
abstract class SendEmailFormFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(SendEmailFormFragment::class)
    abstract fun bindSendEmailFormFragmentInjectorFactory(builder: SendEmailFormFragmentSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}