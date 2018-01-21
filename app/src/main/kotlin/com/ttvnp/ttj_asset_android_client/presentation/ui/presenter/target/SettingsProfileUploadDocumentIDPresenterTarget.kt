package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

interface SettingsProfileUploadDocumentIDPresenterTarget : BasePresenterTarget {
    fun setDocumentID(idDocument1ImageURL: String, idDocument2ImageURL: String)
    fun showMessageOnUploadSuccessfullyCompleted()
}