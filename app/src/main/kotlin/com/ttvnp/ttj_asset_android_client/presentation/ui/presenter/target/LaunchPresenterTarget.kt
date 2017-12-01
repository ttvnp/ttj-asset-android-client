package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

interface LaunchPresenterTarget : BasePresenterTarget {
    fun startNextActivity(isDeviceReady: Boolean)
}