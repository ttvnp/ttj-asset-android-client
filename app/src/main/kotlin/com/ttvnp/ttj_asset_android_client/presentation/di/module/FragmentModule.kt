package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenterImpl
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    // presenters
    @Provides
    fun mainHomePresenter(): MainHomePresenter {
        return MainHomePresenterImpl()
    }
}