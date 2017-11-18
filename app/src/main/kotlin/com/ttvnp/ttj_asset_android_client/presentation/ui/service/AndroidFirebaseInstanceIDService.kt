package com.ttvnp.ttj_asset_android_client.presentation.ui.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AndroidFirebaseInstanceIDService : FirebaseInstanceIdService() {

    @Inject
    lateinit var deviceUseCase : DeviceUseCase

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        refreshedToken?.let { deviceToken ->
            val disposables = CompositeDisposable()
            deviceUseCase.updateDeviceToken(deviceToken)
                    .subscribeOn(Schedulers.io())
                    .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                        override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                            Log.d(javaClass.name, "Refreshed token: " + deviceToken)
                        }
                        override fun onError(e: Throwable) {
                            Log.e(javaClass.name, "Error on refreshing token: " + deviceToken)
                        }
                    }).addTo(disposables)
        }
    }
}
