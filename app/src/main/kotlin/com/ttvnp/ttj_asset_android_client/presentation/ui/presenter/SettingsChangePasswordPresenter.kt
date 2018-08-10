package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import android.content.Context
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsChangePasswordPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SettingsChangePasswordPresenter {
    fun init(target: SettingsChangePasswordPresenterTarget)
    fun changePassword(oldPassword: String, newPassword: String, retypePassword: String)
    fun isValidated(context: Context, oldPassword: String, newPassword: String, retypePassword: String): Boolean
}

class SettingsChangePasswordPresenterImpl @Inject constructor(
        val userUseCase: UserUseCase
): BasePresenter(), SettingsChangePasswordPresenter {

    private lateinit var target: SettingsChangePasswordPresenterTarget

    override fun init(target: SettingsChangePasswordPresenterTarget) {
        this.target = target
    }

    override fun changePassword(
            oldPassword: String,
            newPassword: String,
            retypePassword: String
    ) {
        userUseCase.changePassword(oldPassword, newPassword, retypePassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {
                    override fun onOtherError(error: Throwable?) {
                        target.dismissProgressDialog()
                        error?.let { target.showError(error) }
                    }

                    override fun onMaintenance() {
                        target.showMaintenance()
                    }

                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>?) {
                        wrapper?.let {
                            when (it.errorCode) {
                                ErrorCode.NO_ERROR -> target.onChangePasswordSuccessful()
                                ErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT -> target.showError(it.errorCode, it.error)
                                else -> target.showError(it.errorCode, it.error)
                            }
                        }
                    }
                })
    }

    override fun isValidated(
            context: Context,
            oldPassword: String,
            newPassword: String,
            retypePassword: String
    ): Boolean {
        var oldPasswordError: String? = null
        var newPasswordError: String? = null
        var retypePasswordError: String? = null

        if (oldPassword.isEmpty()) {
            oldPasswordError = context.getString(R.string.please_input_old_password)
        }

        if (newPassword.count() <= 6) {
            newPasswordError = context.getString(R.string.password_should_be_than_6_characters_or_numbers)
        }

        if (newPassword.isEmpty()) {
            newPasswordError = context.getString(R.string.please_input_new_password)
        }

        if (newPassword != retypePassword) {
            retypePasswordError = context.getString(R.string.new_passwords_are_not_matched)
        }

        if (retypePassword.isEmpty()) {
            retypePasswordError = context.getString(R.string.please_input_retype_password)
        }

        target.onValidateForm(oldPasswordError, newPasswordError, retypePasswordError)

        if (oldPassword.isEmpty() || newPassword.isEmpty() || newPassword.count() <= 6
                || retypePassword.isEmpty() || newPassword != retypePassword) {
            return false
        }

        return true
    }

}