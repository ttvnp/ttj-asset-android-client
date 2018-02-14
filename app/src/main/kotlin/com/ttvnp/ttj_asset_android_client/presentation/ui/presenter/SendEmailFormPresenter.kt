package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendEmailFormPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendEmailFormPresenter {
    fun initialize(target: SendEmailFormPresenterTarget)
    fun checkEmailAddress(emailAddress: String, handleSuccess: (OtherUserModel) -> Unit)
}

class SendEmailFormPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendEmailFormPresenter {

    private var target: SendEmailFormPresenterTarget? = null

    override fun initialize(target: SendEmailFormPresenterTarget) {
        this.target = target
    }

    override fun checkEmailAddress(emailAddress: String, handleSuccess: (OtherUserModel) -> Unit) {
        userUseCase.getTargetUser(emailAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<OtherUserModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<OtherUserModel?>) {
                        when (wrapper.errorCode) {
                            ErrorCode.NO_ERROR -> handleSuccess(wrapper.model!!)
                            else -> target?.showError(wrapper.errorCode, wrapper.error)
                        }
                    }
                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }
                }).addTo(this.disposables)
    }
}
