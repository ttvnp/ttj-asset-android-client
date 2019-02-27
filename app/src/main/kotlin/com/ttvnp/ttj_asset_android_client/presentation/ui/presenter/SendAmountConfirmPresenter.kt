package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountConfirmPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendAmountConfirmPresenter {
    fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel)
    fun getUserInfo()
    fun createTransaction(sendInfoModel: SendInfoModel, password: String)
    fun createExternalTransaction(sendInfoModel: SendInfoModel, password: String)
    fun isValid(password: String): Boolean
    fun dispose()
}

class SendAmountConfirmPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendAmountConfirmPresenter {

    private var target: SendAmountConfirmPresenterTarget? = null

    override fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel) {
        this.target = target
        target.setSendInfo(sendInfoModel)
    }

    override fun getUserInfo() {
        userUseCase.getUser(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target?.showProgressDialog()
                }
                .doFinally {
                    target?.dismissProgressDialog()
                }
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {
                    override fun onOtherError(error: Throwable?) {
                        // do nothing
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                    override fun onSuccess(userModel: UserModel) {
                        userModel.let {
                            target?.onBindUserInfo(it)
                        }
                    }
                }).addTo(this.disposables)
    }

    override fun createTransaction(sendInfoModel: SendInfoModel, password: String) {
        userUseCase.createTransaction(sendInfoModel, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target?.showProgressDialog()
                }
                .doFinally {
                    target?.dismissProgressDialog()
                }
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserTransactionModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<UserTransactionModel?>) {
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> target?.onTransactionSuccess(sendInfoModel)
                            else -> target?.showError(wrapper.errorCode, wrapper.error)
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun createExternalTransaction(sendInfoModel: SendInfoModel, password: String) {
        userUseCase.createExternalTransaction(
                sendInfoModel.targetUserStrAccountID,
                sendInfoModel.targetUserStrMemoText,
                sendInfoModel.assetType,
                sendInfoModel.amount, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    target?.showProgressDialog()
                }
                .doFinally {
                    target?.dismissProgressDialog()
                }
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserTransactionModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<UserTransactionModel?>) {
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> target?.onExternalTransactionSuccess(sendInfoModel)
                            else -> target?.showError(wrapper.errorCode, wrapper.error)
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun isValid(password: String): Boolean {
        var passwordError: Int? = null
        if (password.isEmpty()) {
            passwordError = R.string.please_input_password
            target?.validateForm(passwordError)
            return false
        }
        target?.validateForm(passwordError)
        return true
    }

}