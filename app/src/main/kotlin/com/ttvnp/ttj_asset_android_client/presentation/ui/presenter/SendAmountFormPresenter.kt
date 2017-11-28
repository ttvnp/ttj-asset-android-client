package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormPresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface SendAmountFormPresenter {
    fun initialize(target: SendAmountFormPresenterTarget, qrCodeString: String)
}

class SendAmountFormPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SendAmountFormPresenter {

    private var target: SendAmountFormPresenterTarget? = null

    override fun initialize(target: SendAmountFormPresenterTarget, qrCodeString: String) {
        this.target = target
        try {
            val qrCodeInfo = QRCodeInfoModel.load(qrCodeString)
            userUseCase.getTargetUser(qrCodeInfo.emailAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ModelWrapper<OtherUserModel?>>() {
                        override fun onSuccess(wrapper: ModelWrapper<OtherUserModel?>) {
                            when (wrapper.errorCode) {
                                ErrorCode.NO_ERROR -> {
                                    // show info
                                    val model = wrapper.model!!
                                    val sendInfoModel = SendInfoModel(
                                            targetUserID = model.id,
                                            targetUserEmailAddress = model.emailAddress,
                                            targetUserProfileImageID = model.profileImageID,
                                            targetUserProfileImageURL = model.profileImageURL,
                                            targetUserFirstName = model.firstName,
                                            targetUserMiddleName = model.middleName,
                                            targetUserLastName = model.lastName,
                                            assetType = qrCodeInfo.assetType,
                                            amount = qrCodeInfo.amount
                                    )
                                    target.setSendInfo(sendInfoModel)
                                }
                                else -> target.showError(wrapper.errorCode, wrapper.error)
                            }
                        }
                        override fun onError(e: Throwable) {
                            target.showError(e)
                        }
                    }).addTo(this.disposables)
        } catch (t: Throwable) {
            target.showError(t)
        }
    }
}
