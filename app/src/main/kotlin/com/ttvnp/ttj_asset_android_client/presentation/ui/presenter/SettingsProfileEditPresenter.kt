package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileEditPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

interface SettingsProfileEditPresenter {
    fun initialize(target: SettingsProfileEditPresenterTarget)
    fun setupUserInfo()
    fun updateUserInfo(
            profileImageFile: File?,
            firstName: String,
            middleName: String,
            lastName: String,
            address: String,
            genderType: Int,
            dob: String,
            cellphoneNumberNationalCode: String,
            cellphoneNumber: String
    )

    fun dispose()
}

class SettingsProfileEditPresenterImpl @Inject constructor(
        val userUseCase: UserUseCase
) : BasePresenter(), SettingsProfileEditPresenter {

    private var target: SettingsProfileEditPresenterTarget? = null

    override fun initialize(target: SettingsProfileEditPresenterTarget) {
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
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun updateUserInfo(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String, genderType: Int, dob: String, cellphoneNumberNationalCode: String, cellphoneNumber: String) {
        target?.showProgressDialog()
        userUseCase.updateUser(profileImageFile, firstName, middleName, lastName, address, genderType, dob, cellphoneNumberNationalCode, cellphoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>) {
                        target?.dismissProgressDialog()
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                wrapper.model?.let {
                                    target?.showMessageOnUpdateSuccessfullyCompleted()
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