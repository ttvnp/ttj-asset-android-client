package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.SettingsProfileUploadDocumentIDSubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileUploadDocumentIDFragment
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [SettingsProfileUploadDocumentIDSubcomponent::class])
abstract class SettingsProfileUploadDocumentIDFragmentModule {
    @Binds
    @IntoMap
    @ClassKey(SettingsProfileUploadDocumentIDFragment::class)
    abstract fun bindSettingsProfileUploadDocumentIDFragmentInjectFactory(factory: SettingsProfileUploadDocumentIDSubcomponent.Factory): AndroidInjector.Factory<*>
}