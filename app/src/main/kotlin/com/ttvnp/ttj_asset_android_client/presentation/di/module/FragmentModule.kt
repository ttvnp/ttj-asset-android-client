package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.*
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    // presenters
    @Provides
    fun mainHomePresenter(userUseCase: UserUseCase): MainHomePresenter {
        return MainHomePresenterImpl(userUseCase)
    }

    @Provides
    fun mainReceivePresenter(userUseCase: UserUseCase): MainReceivePresenter {
        return MainReceivePresenterImpl(userUseCase)
    }

    @Provides
    fun mainSendPresenter(userUseCase: UserUseCase): MainSendPresenter {
        return MainSendPresenterImpl(userUseCase)
    }

    @Provides
    fun mainSettingsPresenter(deviceUseCase: DeviceUseCase): MainSettingsPresenter {
        return MainSettingsPresenterImpl(deviceUseCase)
    }

    @Provides
    fun sendEmailFormPresenter(userUseCase: UserUseCase): SendEmailFormPresenter {
        return SendEmailFormPresenterImpl(userUseCase)
    }

    @Provides
    fun sendAmountFormPresenter(userUseCase: UserUseCase): SendAmountFormPresenter {
        return SendAmountFormPresenterImpl(userUseCase)
    }

    @Provides
    fun sendAmountFormByStellarPresenter(userUseCase: UserUseCase): SendAmountFormByStellarPresenter {
        return SendAmountFormByStellarPresenterImpl(userUseCase)
    }

    @Provides
    fun sendAmountConfirmPresenter(userUseCase: UserUseCase): SendAmountConfirmPresenter {
        return SendAmountConfirmPresenterImpl(userUseCase)
    }

    @Provides
    fun settingsProfileDetailPresenter(userUseCase: UserUseCase): SettingsProfileDetailPresenter {
        return SettingsProfileDetailPresenterImpl(userUseCase)
    }

    @Provides
    fun settingsProfileEditPresenter(userUseCase: UserUseCase): SettingsProfileEditPresenter {
        return SettingsProfileEditPresenterImpl(userUseCase)
    }

    @Provides
    fun settingsProfileUploadDocumentIDPresenter(userUseCase: UserUseCase): SettingsProfileUploadDocumentIDPresenter {
        return SettingsProfileUploadDocumentIDPresenterImpl(userUseCase)
    }
}