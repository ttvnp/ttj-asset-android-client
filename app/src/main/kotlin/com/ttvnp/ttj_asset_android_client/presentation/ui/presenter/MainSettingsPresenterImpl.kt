package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.LogoutModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainSettingsPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainSettingsPresenter {
    fun init(target: MainSettingsPresenterTarget)
    fun saveLanguage(language: String)
    fun logout()
    fun dispose()
}

class MainSettingsPresenterImpl @Inject constructor(
        val deviceUseCase: DeviceUseCase
) : BasePresenter(), MainSettingsPresenter {

    private lateinit var target: MainSettingsPresenterTarget

    override fun init(target: MainSettingsPresenterTarget) {
        this.target = target
    }

    override fun saveLanguage(language: String) {
        deviceUseCase.saveLanguage(language)
    }

    override fun logout() {
        target.showProgressDialog()
        deviceUseCase.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<LogoutModel?>>() {
                    override fun onOtherError(error: Throwable?) {
                        error?.let {
                            target.dismissProgressDialog()
                            target.showError(error)
                        }
                    }

                    override fun onMaintenance() {
                        target.dismissProgressDialog()
                        target.showMaintenance()
                    }

                    override fun onSuccess(wrapper: ModelWrapper<LogoutModel?>) {
                        target.dismissProgressDialog()
                        wrapper.let {
                            when (it.errorCode) {
                                ErrorCode.NO_ERROR -> {
                                    target.onLogoutSuccessfully(it.model?.isLogout ?: false)
                                }
                                else -> {
                                    target.showError(it.errorCode, it.error)
                                }
                            }
                        }
                    }
                }).addTo(this.disposables)
    }
}