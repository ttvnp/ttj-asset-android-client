package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import javax.inject.Inject

interface MainSettingsPresenter {
    fun saveLanguage(language: String)
}
class MainSettingsPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : MainSettingsPresenter{

    override fun saveLanguage(language: String) {
        deviceUseCase.saveLanguage(language)
    }

}