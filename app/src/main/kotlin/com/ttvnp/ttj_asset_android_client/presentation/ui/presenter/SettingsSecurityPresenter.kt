package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsSecurityPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsSecurityPresenter {
    fun init(target: SettingsSecurityPresenterTarget)
    fun updateSecuritySettings(requirePasswordOnSend: Boolean)
}

class SettingsSecurityPresenterImpl @Inject constructor(
        val userUseCase: UserUseCase
) : BasePresenter(), SettingsSecurityPresenter {

    private lateinit var target: SettingsSecurityPresenterTarget

    override fun init(target: SettingsSecurityPresenterTarget) {
        this.target = target
        userUseCase.getUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {
                    override fun onOtherError(error: Throwable?) {
                        // do nothing
                    }

                    override fun onMaintenance() {
                        target.showMaintenance()
                    }

                    override fun onSuccess(userModel: UserModel?) {
                        userModel?.let {
                            target.bindUserInfo(it)
                        }
                    }
                })
    }

    override fun updateSecuritySettings(requirePasswordOnSend: Boolean) {
        target.showProgressDialog()
        userUseCase.updateSecuritySettings(requirePasswordOnSend)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {

                    override fun onOtherError(error: Throwable?) {
                        target.dismissProgressDialog()
                        error?.let { target.showError(error) }
                    }

                    override fun onMaintenance() {
                        target.dismissProgressDialog()
                        target.showMaintenance()
                    }

                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>?) {
                        target.dismissProgressDialog()
                        wrapper?.let {
                            when (it.errorCode) {
                                ErrorCode.NO_ERROR -> {
                                    it.model?.let {
                                        target.bindUserInfo(it)
                                    }
                                }
                                else -> {
                                    target.showError(it.errorCode, it.error)
                                }
                            }
                        }
                    }
                }).addTo(disposables)
    }

}