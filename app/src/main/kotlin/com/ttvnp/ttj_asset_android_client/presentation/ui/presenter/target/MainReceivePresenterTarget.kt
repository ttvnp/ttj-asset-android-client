package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.StellarAccountModel

interface MainReceivePresenterTarget : BasePresenterTarget {
    fun setQRCode(qrText: String)
    fun onGettingStellarAccount(stellarAccountModel: StellarAccountModel)
    fun onError()
}