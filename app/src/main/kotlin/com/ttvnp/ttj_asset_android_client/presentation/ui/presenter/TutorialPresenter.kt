package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
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
    fun submitEmailAddress()
}

class TutorialPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : BasePresenter(), TutorialPresenter {

    private var target: TutorialPresenterTarget? = null

    override fun onCreate(target: TutorialPresenterTarget) {
        this.target = target
    }

    override fun start() {
        deviceUseCase.init()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<DeviceModel>() {
                    override fun onComplete() { }
                    override fun onNext(t: DeviceModel) {
                        target?.gotoFormPage()
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun submitEmailAddress() {
        // check if email address is valid.
        // on success register email address.
        //   on success navigate to next page.
        //   on failure show error.
        // on error show error message.
    }
}

