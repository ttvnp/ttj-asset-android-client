package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.TutorialPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface TutorialPresenter {
    fun onCreate(target: TutorialPresenterTarget)
    fun start()
    fun submitEmailAddress(emailAddress: String)
    fun verifyEmailAddress(verificationCode: String, passwordOnImport: String)
    fun dispose()
}

class TutorialPresenterImpl @Inject constructor(
        val deviceUseCase: DeviceUseCase
) : BasePresenter(), TutorialPresenter {

    private var target: TutorialPresenterTarget? = null

    override fun onCreate(target: TutorialPresenterTarget) {
        this.target = target
    }

    override fun start() {
        target?.showProgressDialog()
        deviceUseCase.init()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<DeviceModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                target?.gotoRegisterEmailPage()
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun submitEmailAddress(emailAddress: String) {
        target?.showProgressDialog()
        deviceUseCase.registerEmail(emailAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<RegisterEmailResultModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<RegisterEmailResultModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                target?.gotoVerifyEmailPage(wrapper.model!!)
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun verifyEmailAddress(verificationCode: String, passwordOnImport: String) {
        target?.showProgressDialog()
        deviceUseCase.verifyEmail(verificationCode, passwordOnImport)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                target?.gotoEndPage()
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }
}

