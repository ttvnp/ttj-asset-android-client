package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountConfirmPresenterTarget
import javax.inject.Inject

interface SendAmountConfirmPresenter {
    fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel)
}

class SendAmountConfirmPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendAmountConfirmPresenter {

    private var target: SendAmountConfirmPresenterTarget? = null

    override fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel) {
        this.target = target
        target.setSendInfo(sendInfoModel)
    }
}