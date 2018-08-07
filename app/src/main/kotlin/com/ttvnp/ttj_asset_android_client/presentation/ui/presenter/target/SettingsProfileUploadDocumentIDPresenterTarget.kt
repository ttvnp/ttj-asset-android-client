package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.UserModel

interface SettingsProfileUploadDocumentIDPresenterTarget : BasePresenterTarget {
    fun setDocumentID(userModel: UserModel)
    fun showMessageOnUploadSuccessfullyCompleted()
}