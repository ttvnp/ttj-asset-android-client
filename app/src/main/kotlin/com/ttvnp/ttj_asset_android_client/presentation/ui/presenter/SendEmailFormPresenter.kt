package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendEmailFormPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.CompositeException
import io.reactivex.observers.DisposableSingleObserver
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
        try {
            userUseCase.getTargetUser(emailAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<OtherUserModel>() {
                        override fun onSuccess(t: OtherUserModel) {
                            handleSuccess(t)
                        }
                        override fun onError(e: Throwable) {
                            val error = if (e is CompositeException && 0 < e.exceptions.size) e.exceptions.first() else e
                            when (error) {
                                is ServiceFailedException -> target?.showNoSuchUser()
                                else -> target?.showError(e)
                            }
                        }
                    }).addTo(this.disposables)
        } catch (t: Throwable) {
            target?.showError(t)
        }
    }
}
