package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException

interface TutorialPresenterTarget : BasePresenterTarget {
    fun gotoRegisterEmailPage()
    fun showValidationError(ve: ValidationException)
    fun gotoVerifyEmailPage()
    fun gotoEndPage()
}
