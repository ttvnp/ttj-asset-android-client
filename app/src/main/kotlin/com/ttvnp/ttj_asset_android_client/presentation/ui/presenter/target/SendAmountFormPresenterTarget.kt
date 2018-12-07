package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel

interface SendAmountFormPresenterTarget : BasePresenterTarget {
    fun setSendInfo(sendInfoModel: SendInfoModel)
    fun navigateToConfirm(assetType: AssetType, amount: Long)
    fun onValidation(amountError: Int?)
}