package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountConfirmPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendAmountConfirmPresenter {
    fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel)
    fun createTransaction(sendInfoModel: SendInfoModel)
}

class SendAmountConfirmPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendAmountConfirmPresenter {

    private var target: SendAmountConfirmPresenterTarget? = null

    override fun initialize(target: SendAmountConfirmPresenterTarget, sendInfoModel: SendInfoModel) {
        this.target = target
        target.setSendInfo(sendInfoModel)
    }

    override fun createTransaction(sendInfoModel: SendInfoModel) {
        target?.showProgressDialog()
        userUseCase.createTransaction(sendInfoModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserTransactionModel>() {
                    override fun onSuccess(m: UserTransactionModel) {
                        target?.dismissProgressDialog()
                        target?.onTransactionSuccess()
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }
}