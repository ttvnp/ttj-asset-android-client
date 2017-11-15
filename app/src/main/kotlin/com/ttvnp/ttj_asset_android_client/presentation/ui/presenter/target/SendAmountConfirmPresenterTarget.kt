package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel

interface SendAmountConfirmPresenterTarget : BasePresenterTarget {
    fun setSendInfo(sendInfoModel: SendInfoModel)
    fun onTransactionSuccess()
}