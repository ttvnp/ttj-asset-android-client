package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

interface SettingsChangePasswordPresenterTarget : BasePresenterTarget {
    fun onChangePasswordSuccessful()
    fun onValidateForm(oldPasswordError: String?,
                       newPasswordError: String?,
                       retypePasswordError: String?)
}