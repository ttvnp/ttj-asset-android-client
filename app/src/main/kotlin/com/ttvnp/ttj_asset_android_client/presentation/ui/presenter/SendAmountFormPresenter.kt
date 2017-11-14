package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
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
                    .subscribeWith(object : DisposableSingleObserver<OtherUserModel>() {
                        override fun onSuccess(t: OtherUserModel) {
                            // show info
                            val sendInfoModel = SendInfoModel(
                                    targetUserID = t.id,
                                    targetUserEmailAddress = t.emailAddress,
                                    targetUserProfileImageID = t.profileImageID,
                                    targetUserProfileImageURL = t.profileImageURL,
                                    targetUserFirstName = t.firstName,
                                    targetUserMiddleName = t.middleName,
                                    targetUserLastName = t.lastName,
                                    assetType = qrCodeInfo.assetType,
                                    amount = qrCodeInfo.amount
                            )
                            target.setSendInfo(sendInfoModel)
                        }
                        override fun onError(e: Throwable) {
                            // do nothing...
                        }
                    }).addTo(this.disposables)
        } catch (t: Throwable) {
            target.showError(t)
        }
    }
}