package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainReceivePresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainReceivePresenter {
    fun init(target: MainReceivePresenterTarget)
    fun setupDefault()
}

class MainReceivePresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainReceivePresenter {

    private var target: MainReceivePresenterTarget? = null

    override fun init(target: MainReceivePresenterTarget) {
        this.target = target
    }

    override fun setupDefault() {
        userUseCase.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserModel>() {
                    override fun onSuccess(t: UserModel) {
                        target?.setQRCode(QRCodeInfoModel(emailAddress = t.emailAddress).toQRString())
                    }
                    override fun onError(e: Throwable) {
                        // do nothing...
                    }
                }).addTo(this.disposables)
    }
}
