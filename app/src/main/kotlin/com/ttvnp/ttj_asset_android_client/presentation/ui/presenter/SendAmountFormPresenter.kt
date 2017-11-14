package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormPresenterTarget
import javax.inject.Inject

interface SendAmountFormPresenter {
    fun initialize(target: SendAmountFormPresenterTarget, qrCodeString: String)
}

class SendAmountFormPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendAmountFormPresenter {

    private var target: SendAmountFormPresenterTarget? = null

    override fun initialize(target: SendAmountFormPresenterTarget, qrCodeString: String) {
        this.target = target
        try {
            val qrCodeInfo = QRCodeInfoModel.load(qrCodeString)
            // TODO get user by email address
            // show info
            val sendInfoModel = SendInfoModel(
                    targetUserEmailAddress = qrCodeInfo.emailAddress,
                    assetType = qrCodeInfo.assetType,
                    amount = qrCodeInfo.amount
            )
            target.setSendInfo(sendInfoModel)
        } catch (t: Throwable) {
            target.showError(t)
        }
    }
}