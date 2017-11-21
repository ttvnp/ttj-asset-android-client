package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.exceptions.BaseException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.TutorialPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface TutorialPresenter {
    fun onCreate(target: TutorialPresenterTarget)
    fun start()
    fun submitEmailAddress(emailAddress: String, handleValidationError: (Throwable) -> Unit)
    fun verifyEmailAddress(verificationCode: String, handleValidationError: (Throwable) -> Unit)
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
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
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
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun submitEmailAddress(emailAddress: String, handleValidationError: (Throwable) -> Unit) {
        try {
            target?.showProgressDialog()
            deviceUseCase.registerEmail(emailAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<DeviceModel>() {
                        override fun onSuccess(t: DeviceModel) {
                            target?.dismissProgressDialog()
                            target?.gotoVerifyEmailPage()
                        }
                        override fun onError(e: Throwable) {
                            target?.dismissProgressDialog()
                            when(e) {
                                is BaseException -> handleValidationError(e)
                                else -> target?.showError(e)
                            }
                        }
                    }).addTo(this.disposables)
        } catch (e: Throwable) {
            target?.dismissProgressDialog()
            when(e) {
                is BaseException -> handleValidationError(e)
                else -> target?.showError(e)
            }
        }
    }

    override fun verifyEmailAddress(verificationCode: String, handleValidationError: (Throwable) -> Unit) {
        target?.showProgressDialog()
        deviceUseCase.verifyEmail(verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserModel>() {
                    override fun onSuccess(t: UserModel) {
                        target?.dismissProgressDialog()
                        target?.gotoEndPage()
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        when(e) {
                            is BaseException -> handleValidationError(e)
                            else -> target?.showError(e)
                        }
                    }
                }).addTo(this.disposables)
    }
}

