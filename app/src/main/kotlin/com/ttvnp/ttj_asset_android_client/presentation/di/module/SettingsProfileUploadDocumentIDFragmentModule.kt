package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.support.v4.app.Fragment
import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileUploadDocumentIDSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileUploadDocumentIDFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(SettingsProfileUploadDocumentIDSubcomponent::class))
abstract class SettingsProfileUploadDocumentIDFragmentModule {
    @Binds
    @IntoMap
    @FragmentKey(SettingsProfileUploadDocumentIDFragment::class)
    abstract fun bindSettingsProfileUploadDocumentIDFragmentInjectFactory(builder: SettingsProfileUploadDocumentIDSubcomponent.Builder): AndroidInjector.Factory<out Fragment>
}