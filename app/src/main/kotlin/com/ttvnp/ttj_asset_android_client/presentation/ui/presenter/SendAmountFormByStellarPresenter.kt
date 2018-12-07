package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.domain.util.toAmount
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormByStellarPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendAmountFormByStellarPresenter {
    fun initialize(target: SendAmountFormByStellarPresenterTarget)
    fun checkValidationStellar(accountId: String, amountString: String, assetType: AssetType)
    fun checkSendAmount(assetType: AssetType, amountString: String)
    fun isValid(address: String, amount: String): Boolean
    fun dispose()
}

class SendAmountFormByStellarPresenterImpl @Inject constructor(
        val userUseCase: UserUseCase
) : SendAmountFormByStellarPresenter, BasePresenter() {

    private lateinit var target: SendAmountFormByStellarPresenterTarget

    override fun initialize(target: SendAmountFormByStellarPresenterTarget) {
        this.target = target
    }

    override fun checkValidationStellar(accountId: String, amountString: String, assetType: AssetType) {
        userUseCase.checkValidationStellar(accountId, assetType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target.showProgressDialog()
                }
                .doFinally {
                    target.dismissProgressDialog()
                }
                .subscribeWith(object : DisposableApiSingleObserver<ErrorCode>() {

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target.showError(error) }
                    }

                    override fun onMaintenance() {
                        target.showMaintenance()
                    }

                    override fun onSuccess(errorCode: ErrorCode) {
                        when (errorCode) {
                            ErrorCode.NO_ERROR -> checkSendAmount(assetType, amountString)
                            ErrorCode.ERROR_VALIDATION_STELLAR_ACCOUNT -> target.showError(errorCode, null)
                            ErrorCode.ERROR_VALIDATION_STELLAR_TRUST_LINE -> target.showError(errorCode, null)
                            else -> target.showError(errorCode, null)
                        }
                    }

                }).addTo(this.disposables)
    }

    override fun checkSendAmount(assetType: AssetType, amountString: String) {
        userUseCase.checkSendAmount(assetType, amountString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target.showProgressDialog()
                }
                .doFinally {
                    target.dismissProgressDialog()
                }
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

    override fun isValid(address: String, amount: String): Boolean {
        var addressError: Int? = null
        var amountError: Int? = null
        if (address.isEmpty()) {
            addressError = R.string.please_input_address
        }
        if (amount.isEmpty()) {
            amountError = R.string.error_message_invalid_long
        }
        target.onValidation(addressError, amountError)
        return address.isNotEmpty() && amount.isNotEmpty()
    }

}