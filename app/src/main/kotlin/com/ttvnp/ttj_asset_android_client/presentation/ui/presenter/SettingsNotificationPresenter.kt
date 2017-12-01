package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsNotificationPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsNotificationPresenter {
    fun setupNotificationInfo(target: SettingsNotificationPresenterTarget)
    fun updateGrantPushNotification(grantPushNotification: Boolean)
    fun updateGrantEmailNotification(grantEmailNotification: Boolean)
}

class SettingsNotificationPresenterImpl @Inject constructor(val deviceUseCase: DeviceUseCase) : BasePresenter(), SettingsNotificationPresenter {

    private var target: SettingsNotificationPresenterTarget? = null


    override fun setupNotificationInfo(target: SettingsNotificationPresenterTarget) {
        this.target = target
        deviceUseCase.getDevice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        wrapper.model?.let {
                            target.bindDeviceInfo(it)
                        }
                    }
                    override fun onError(e: Throwable) {
                        target.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun updateGrantPushNotification(grantPushNotification: Boolean) {
        target?.showProgressDialog()
        deviceUseCase.updateGrantPushNotification(grantPushNotification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                wrapper.model?.let {
                                    target?.bindDeviceInfo(it)
                                }
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun updateGrantEmailNotification(grantEmailNotification: Boolean) {
        target?.showProgressDialog()
        deviceUseCase.updateGrantEmailNotification(grantEmailNotification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                wrapper.model?.let {
                                    target?.bindDeviceInfo(it)
                                }
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }
                    override fun onError(e: Throwable) {
                        target?.dismissProgressDialog()
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }
}
