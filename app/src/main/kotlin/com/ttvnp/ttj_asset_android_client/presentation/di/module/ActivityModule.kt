package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.*
import dagger.Module
import dagger.Provides

@Module
class ActivityModule {

    // presenters
    @Provides
    fun launchPresenter(deviceUseCase: DeviceUseCase): LaunchPresenter {
        return LaunchPresenterImpl(deviceUseCase)
    }

    @Provides
    fun tutorialPresenter(deviceUseCase: DeviceUseCase): TutorialPresenter {
        return TutorialPresenterImpl(deviceUseCase)
    }

    @Provides
    fun receiveSetAmountPresenter(userUseCase: UserUseCase): ReceiveSetAmountPresenter {
        return ReceiveSetAmountPresenterImpl(userUseCase)
    }

    @Provides
    fun settingsNotificationPresenter(deviceUseCase: DeviceUseCase, userUseCase: UserUseCase): SettingsNotificationPresenter {
        return SettingsNotificationPresenterImpl(deviceUseCase, userUseCase)
    }

    @Provides
    fun settingsChangePasswordPresenter(userUseCase: UserUseCase): SettingsChangePasswordPresenter {
        return SettingsChangePasswordPresenterImpl(userUseCase)
    }

    @Provides
    fun settingsSecurityPresenter(userUseCase: UserUseCase): SettingsSecurityPresenter {
        return SettingsSecurityPresenterImpl(userUseCase)
    }

}
