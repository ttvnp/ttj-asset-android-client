package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

interface MainSendPresenterTarget : BasePresenterTarget {
    fun setIdentify(identifier: Boolean)
    fun preRequest()
    fun postRequest()
}