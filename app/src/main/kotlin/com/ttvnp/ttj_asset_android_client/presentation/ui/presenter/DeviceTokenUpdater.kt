package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.iid.FirebaseInstanceId
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class DeviceTokenUpdater @Inject constructor(val deviceUseCase: DeviceUseCase) {

    private val disposables = CompositeDisposable()

    fun updateDeviceTokenIfEmpty() {
        deviceUseCase.getDevice()
                .subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        wrapper.model?.let {
                            if (it.deviceToken.isBlank() || it.deviceToken != FirebaseInstanceId.getInstance().getToken()) {
                                updateDeviceToken()
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        FirebaseCrash.report(e)
                    }
                }).addTo(disposables)
    }

    fun updateDeviceToken() {
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        refreshedToken?.let { deviceToken ->
            deviceUseCase.updateDeviceToken(deviceToken)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                        override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                            Log.d(javaClass.name, "Refreshed token: " + deviceToken)
                        }
                        override fun onError(e: Throwable) {
                            FirebaseCrash.report(e)
                        }
                    }).addTo(disposables)
        }
    }
}