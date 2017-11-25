package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.LaunchPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface LaunchPresenter {
    fun initialize(target: LaunchPresenterTarget)
    fun checkDeviceReady(handleResult: (Boolean) -> Unit)
}

class LaunchPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : LaunchPresenter {

    private var target:LaunchPresenterTarget? = null
    private val disposables = CompositeDisposable()

    override fun initialize(target: LaunchPresenterTarget) {
        this.target = target
    }

    override fun checkDeviceReady(handleResult: (Boolean) -> Unit) {
        deviceUseCase.getDevice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                if (wrapper.model == null) {
                                    handleResult(false)
                                } else {
                                    handleResult(wrapper.model!!.isActivated)
                                }
                            }
                            ErrorCode.ERROR_DEVICE_NOT_REGISTERED -> {
                                handleResult(false)
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(disposables)
    }
}