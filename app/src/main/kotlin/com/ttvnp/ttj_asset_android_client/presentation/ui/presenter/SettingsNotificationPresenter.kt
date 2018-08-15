package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsNotificationPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsNotificationPresenter {
    fun setupNotificationInfo(target: SettingsNotificationPresenterTarget)
    fun updateGrantPushNotification(grantPushNotification: Boolean)
    fun updateGrantEmailNotification(grantEmailNotification: Boolean)
}

class SettingsNotificationPresenterImpl @Inject constructor(
        val deviceUseCase: DeviceUseCase,
        val userUseCase: UserUseCase
) : BasePresenter(), SettingsNotificationPresenter {

    private var target: SettingsNotificationPresenterTarget? = null

    override fun setupNotificationInfo(target: SettingsNotificationPresenterTarget) {
        this.target = target
        deviceUseCase.getDevice()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<DeviceModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        wrapper.model?.let {
                            target.bindDeviceInfo(it)
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target.showError(error) }
                    }

                    override fun onMaintenance() {
                        target.showMaintenance()
                    }

                }).addTo(this.disposables)
        userUseCase.getUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableApiSingleObserver<UserModel>() {
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
                }).addTo(this.disposables)
    }

    override fun updateGrantPushNotification(grantPushNotification: Boolean) {
        target?.showProgressDialog()
        deviceUseCase.updateGrantPushNotification(grantPushNotification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<DeviceModel?>>() {

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

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun updateGrantEmailNotification(grantEmailNotification: Boolean) {
        target?.showProgressDialog()
        userUseCase.updateGrantEmailNotification(grantEmailNotification)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {

                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                wrapper.model?.let {
                                    target?.bindUserInfo(it)
                                }
                            }
                            else -> {
                                target?.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

}
