package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.domain.util.toAmount
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormByStellarPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendAmountFormByStellarPresenter {
    fun initialize(target: SendAmountFormByStellarPresenterTarget)
    fun checkSendAmount(assetType: AssetType, amountString: String)
}

class SendAmountFormByStellarPresenterImpl @Inject constructor(val userUseCase: UserUseCase)
    : SendAmountFormByStellarPresenter, BasePresenter() {

    private lateinit var target: SendAmountFormByStellarPresenterTarget

    override fun initialize(target: SendAmountFormByStellarPresenterTarget) {
        this.target = target
    }

    override fun checkSendAmount(assetType: AssetType, amountString: String) {
        userUseCase.checkSendAmount(assetType, amountString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ErrorCode>() {

                    override fun onSuccess(errorCode: ErrorCode) {
                        when (errorCode) {
                            ErrorCode.NO_ERROR -> target.navigateToConfirm(assetType, amountString.toAmount())
                            else -> target.showError(errorCode, null)
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target.showError(error) }
                    }

                    override fun onMaintenance() {
                        target.showMaintenance()
                    }

                }).addTo(this.disposables)
    }
}