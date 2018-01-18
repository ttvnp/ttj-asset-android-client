package com.ttvnp.ttj_asset_android_client.presentation.di.component

import com.ttvnp.ttj_asset_android_client.presentation.di.module.*
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent(modules = arrayOf(
        ActivityModule::class,
        DataModule::class,
        DomainModule::class,
        SettingsProfileDetailFragmentModule::class,
        SettingsProfileEditFragmentModule::class,
        SettingsProfileUploadDocumentIDFragmentModule::class
))
interface SettingsProfileActivitySubcomponent : AndroidInjector<SettingsProfileActivity> {
    @Subcomponent.Builder
    abstract class Builder : AndroidInjector.Builder<SettingsProfileActivity>()
}