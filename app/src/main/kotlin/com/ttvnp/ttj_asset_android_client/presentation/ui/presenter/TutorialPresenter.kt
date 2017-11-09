package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.TutorialPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface TutorialPresenter {
    fun onCreate(target: TutorialPresenterTarget)
    fun start()
    fun submitEmailAddress(emailAddress: String)
    fun verifyEmailAddress(verificationCode: String)
}

class TutorialPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : BasePresenter(), TutorialPresenter {

    private var target: TutorialPresenterTarget? = null

    override fun onCreate(target: TutorialPresenterTarget) {
        this.target = target
    }

    override fun start() {
        target?.showProgressDialog()
        deviceUseCase.init()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<DeviceModel>() {
                    override fun onComplete() { }
                    override fun onNext(t: DeviceModel) {
                        target?.dismissProgressDialog()
                        target?.gotoRegisterEmailPage()
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun submitEmailAddress(emailAddress: String) {
        try {
            target?.showProgressDialog()
            deviceUseCase.registerEmail(emailAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableObserver<DeviceModel>() {
                        override fun onComplete() { }
                        override fun onNext(t: DeviceModel) {
                            target?.dismissProgressDialog()
                            target?.gotoVerifyEmailPage()
                        }
                        override fun onError(e: Throwable) {
                            target?.dismissProgressDialog()
                            target?.showError(e)
                        }
                    }).addTo(this.disposables)
        } catch (t: Throwable) {
            target?.dismissProgressDialog()
            when(t) {
                is ValidationException -> target?.showValidationError(t)
                else -> target?.showError(t)
            }
        }
    }

    override fun verifyEmailAddress(verificationCode: String) {
        target?.showProgressDialog()
        deviceUseCase.verifyEmail(verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<UserModel>() {
                    override fun onComplete() { }
                    override fun onNext(t: UserModel) {
                        target?.dismissProgressDialog()
                        target?.gotoEndPage()
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }
}

