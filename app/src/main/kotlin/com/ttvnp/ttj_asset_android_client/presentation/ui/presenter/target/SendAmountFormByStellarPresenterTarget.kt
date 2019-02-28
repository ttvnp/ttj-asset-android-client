package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType

interface SendAmountFormByStellarPresenterTarget : BasePresenterTarget  {
    fun navigateToConfirm(assetType: AssetType, amount: Long)
    fun onValidation(addressError: Int?, memoError: Int?, amountError: Int?)
}