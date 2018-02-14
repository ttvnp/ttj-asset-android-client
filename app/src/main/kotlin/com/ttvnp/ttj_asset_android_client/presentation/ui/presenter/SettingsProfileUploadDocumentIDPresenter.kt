package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileUploadDocumentIDPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.File
import javax.inject.Inject

interface SettingsProfileUploadDocumentIDPresenter {
    fun init(target: SettingsProfileUploadDocumentIDPresenterTarget)
    fun setupDefault()
    fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?)
}

class SettingsProfileUploadDocumentIDPresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), SettingsProfileUploadDocumentIDPresenter {

    private var target: SettingsProfileUploadDocumentIDPresenterTarget? = null

    override fun init(target: SettingsProfileUploadDocumentIDPresenterTarget) {
        this.target = target
    }

    override fun setupDefault() {
        userUseCase.getUser(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserModel>() {
                    override fun onSuccess(userModel: UserModel) {
                        target?.setDocumentID(userModel.isDocument1ImageURL, userModel.isDocument2ImageURL)
                    }

                    override fun onOtherError(error: Throwable?) {
                        // do nothing...
                    }
                }).addTo(this.disposables)
    }

    override fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?) {
        target?.showProgressDialog()
        userUseCase.uploadIdDocument(faceImageFile, addressImageFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<ModelWrapper<UserModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<UserModel?>?) {
                        target?.dismissProgressDialog()
                        when (wrapper?.errorCode) {
                            ErrorCode.NO_ERROR -> {
                                wrapper.model?.let {
                                    target?.showMessageOnUploadSuccessfullyCompleted()
                                    target?.setDocumentID(it.isDocument1ImageURL, it.isDocument2ImageURL)
                                }
                            }
                            else -> {
                                wrapper?.let {
                                    target?.showError(it.errorCode, it.error)
                                }
                            }
                        }
                    }

                    override fun onOtherError(error: Throwable?) {
                        target?.dismissProgressDialog()
                        error?.let { target?.showError(error) }
                    }
                }).addTo(this.disposables)
    }

}