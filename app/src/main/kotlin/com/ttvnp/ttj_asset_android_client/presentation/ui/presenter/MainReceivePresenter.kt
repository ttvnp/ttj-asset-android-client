package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainReceivePresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainReceivePresenter {
    fun init(target: MainReceivePresenterTarget)
    fun setupDefault()
    fun getStellarAccount()
}

class MainReceivePresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainReceivePresenter {

    private var target: MainReceivePresenterTarget? = null

    override fun init(target: MainReceivePresenterTarget) {
        this.target = target
    }

    override fun setupDefault() {
        target?.showProgressDialog()
        userUseCase.getUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {

                    override fun onSuccess(t: UserModel) {
                        target?.dismissProgressDialog()
                        target?.setQRCode(QRCodeInfoModel(
                                emailAddress = t.emailAddress).toQRString())
                    }

                    override fun onOtherError(error: Throwable?) {
                        // do nothing...
                    }

                    override fun onMaintenance() {
                        target?.dismissProgressDialog()
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
        userUseCase.getStellarAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<StellarAccountModel>() {
                    override fun onOtherError(error: Throwable?) {
                        // do nothing...
                    }

                    override fun onMaintenance() {
                        target?.dismissProgressDialog()
                        target?.showMaintenance()
                    }

                    override fun onSuccess(stellarAccountModel: StellarAccountModel) {
                        target?.dismissProgressDialog()
                        target?.onGettingStellarAccount(stellarAccountModel)
                    }
                }).addTo(this.disposables)
    }

    override fun getStellarAccount() {
        target?.showProgressDialog()
        userUseCase.getStellarAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<StellarAccountModel>() {
                    override fun onOtherError(error: Throwable?) {
                    }

                    override fun onMaintenance() {
                        target?.dismissProgressDialog()
                        target?.showMaintenance()
                    }

                    override fun onSuccess(model: StellarAccountModel) {
                        target?.dismissProgressDialog()
                        target?.setQRCode(QRCodeInfoStellarInfoModel(strAccountId = model.strAccountID).toQRString())
                    }
                })
    }
}
