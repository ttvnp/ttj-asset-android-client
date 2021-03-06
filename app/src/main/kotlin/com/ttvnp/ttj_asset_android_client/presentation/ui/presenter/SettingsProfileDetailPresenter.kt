package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileDetailPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsProfileDetailPresenter {
    fun initialize(target: SettingsProfileDetailPresenterTarget)
    fun setupUserInfo()
    fun dispose()
}

class SettingsProfileDetailPresenterImpl @Inject constructor(
        val userUseCase: UserUseCase
) : BasePresenter(), SettingsProfileDetailPresenter {

    private var target: SettingsProfileDetailPresenterTarget? = null

    override fun initialize(target: SettingsProfileDetailPresenterTarget) {
        this.target = target
    }

    override fun setupUserInfo() {
        userUseCase.getUser(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {

                    override fun onSuccess(t: UserModel) {
                        target?.bindUserInfo(t)
                    }

                    override fun onOtherError(error: Throwable?) {
                        // do nothing...
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

}