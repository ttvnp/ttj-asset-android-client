package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import javax.inject.Inject

interface SettingsNotificationPresenter {
    fun setupNotificationInfo(handleSuccess: (ModelWrapper<UserModel?>) -> Unit, handleValidationError: (Throwable) -> Unit)
}

class SettingsNotificationPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SettingsNotificationPresenter {

    override fun setupNotificationInfo(handleSuccess: (ModelWrapper<UserModel?>) -> Unit, handleValidationError: (Throwable) -> Unit) {
        // TODO
    }
}
