package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface LaunchPresenter {
    fun checkDeviceReady(handleResult: (Boolean) -> Unit)
}

class LaunchPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : LaunchPresenter {

    override fun checkDeviceReady(handleResult: (Boolean) -> Unit) {
        val disposables = CompositeDisposable()
        deviceUseCase.getDevice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        when (wrapper.errorCode) {
                            ErrorCode.ERROR_DEVICE_NOT_REGISTERED -> {
                                handleResult(false)
                            }
                            ErrorCode.NO_ERROR -> {
                                if (wrapper.model == null) {
                                    handleResult(false)
                                } else {
                                    handleResult(wrapper.model!!.isActivated)
                                }
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        // TODO handle error such as network error...
                    }
                }).addTo(disposables)
    }
}