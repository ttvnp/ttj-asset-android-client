package com.ttvnp.ttj_asset_android_client.presentation.ui.service

import android.util.Log
import com.google.firebase.crash.FirebaseCrash
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.DeviceTokenUpdater
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AndroidFirebaseInstanceIDService : FirebaseInstanceIdService() {

    @Inject
    lateinit var deviceTokenUpdater : DeviceTokenUpdater

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        deviceTokenUpdater.updateDeviceToken()
    }
}
