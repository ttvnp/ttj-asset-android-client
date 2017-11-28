package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface ReceiveSetAmountPresenter {
    fun submitReceiveInfo(
            assetType: AssetType,
            amount: Long,
            handleSuccess: (QRCodeInfoModel) -> Unit,
            handleValidationError: (Throwable) -> Unit
    )
}

class ReceiveSetAmountPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), ReceiveSetAmountPresenter {

    override fun submitReceiveInfo(
            assetType: AssetType,
            amount: Long,
            handleSuccess: (QRCodeInfoModel) -> Unit,
            handleValidationError: (Throwable) -> Unit
    ) {
        userUseCase.getUser(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserModel>() {
                    override fun onSuccess(t: UserModel) {
                        val model = QRCodeInfoModel(
                                emailAddress = t.emailAddress,
                                assetType = assetType,
                                amount = amount
                        )
                        handleSuccess(model)
                    }
                    override fun onError(e: Throwable) {
                        // do nothing...
                    }
                }).addTo(this.disposables)
    }
}
