package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.RegisterEmailResultModel

interface TutorialPresenterTarget : BasePresenterTarget {
    fun gotoRegisterEmailPage()
    fun gotoVerifyEmailPage(model: RegisterEmailResultModel)
    fun gotoEndPage()
}
