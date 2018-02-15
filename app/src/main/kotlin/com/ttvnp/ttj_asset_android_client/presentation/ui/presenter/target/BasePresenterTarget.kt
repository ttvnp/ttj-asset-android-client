package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode

interface BasePresenterTarget {
    fun showMaintenance()
    fun showError(throwable: Throwable)
    fun showError(errorCode: ErrorCode, throwable: Throwable?)
    fun showProgressDialog()
    fun dismissProgressDialog()
}
