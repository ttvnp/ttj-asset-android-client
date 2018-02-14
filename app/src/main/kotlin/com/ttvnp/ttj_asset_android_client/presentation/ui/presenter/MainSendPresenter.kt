package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainSendPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainSendPresenter {
    fun init(target: MainSendPresenterTarget)
    fun setupDefault()
}

class MainSendPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainSendPresenter {

    private var target: MainSendPresenterTarget? = null

    override fun init(target: MainSendPresenterTarget) {
        this.target = target
    }

    override fun setupDefault() {
        userUseCase.getUser(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {
                    override fun onSuccess(t: UserModel) {
                        target?.setIdentify(t.isIdentified)
                    }

                    override fun onOtherError(error: Throwable?) {
                        // do nothing...
                    }
                }).addTo(this.disposables)
    }
}