package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel

interface SendAmountConfirmPresenterTarget : BasePresenterTarget {
    fun onBindUserInfo(userModel: UserModel)
    fun setSendInfo(sendInfoModel: SendInfoModel)
    fun onTransactionSuccess(sendInfoModel: SendInfoModel)
    fun validateForm(passwordError: Int?)
}