package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import javax.inject.Inject

interface MainHomePresenter {
    fun init(target: MainHomePresenterTarget)
}

class MainHomePresenterImpl @Inject constructor() : BasePresenter(), MainHomePresenter {

    private var target: MainHomePresenterTarget? = null

    override fun init(target: MainHomePresenterTarget) {
        this.target = target
    }
}
