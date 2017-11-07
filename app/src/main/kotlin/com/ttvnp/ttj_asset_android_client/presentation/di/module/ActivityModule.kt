package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.TutorialPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.TutorialPresenterImpl
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    // presenters
    @Provides
    fun tutorialPresenter(deviceUseCase: DeviceUseCase): TutorialPresenter {
        return TutorialPresenterImpl(deviceUseCase)
    }
}
