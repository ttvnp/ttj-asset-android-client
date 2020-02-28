package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoStellarInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.StellarAccountModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainReceivePresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainReceivePresenter {
    fun init(target: MainReceivePresenterTarget)
    fun getStellarAccount()
    fun getUserInfo()
    fun dispose()
}

class MainReceivePresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainReceivePresenter {

    private var target: MainReceivePresenterTarget? = null

    override fun init(target: MainReceivePresenterTarget) {
        this.target = target
    }

    override fun getUserInfo() {
        target?.preRequest()
        userUseCase.getUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {

                    override fun onSuccess(t: UserModel) {
                        target?.postRequest()
                        target?.setQRCode(QRCodeInfoModel(
                                emailAddress = t.emailAddress).toQRString()
                        )
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.postRequest()
                    }

                    override fun onMaintenance() {
                        target?.postRequest()
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun getStellarAccount() {
        target?.showProgressDialog()
        userUseCase.getStellarAccount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<StellarAccountModel>() {

                    override fun onOtherError(error: Throwable?) {
                        target?.postRequest()
                    }

                    override fun onMaintenance() {
                        target?.postRequest()
                        target?.showMaintenance()
                    }

                    override fun onSuccess(model: StellarAccountModel) {
                        target?.postRequest()
                        target?.onGettingStellarAccount(model)
                        target?.setQRCode(QRCodeInfoStellarInfoModel(strAccountId = model.strAccountID).toQRString())
                    }

                }).addTo(this.disposables)
    }

}
