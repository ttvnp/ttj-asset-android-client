package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionsModel
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber.DisposableApiSingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainHomePresenter {
    fun init(target: MainHomePresenterTarget)
    fun setupUserInfo(forceRefresh: Boolean)
    fun setupBalanceInfo(forceRefresh: Boolean)
    fun setupUserTransactions(forceRefresh: Boolean)
    fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit, forceRefresh: Boolean)
    fun onProfileAreaClicked()
    fun dispose()
}

class MainHomePresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainHomePresenter {

    private val historyPageSize: Long = 20L
    private var target: MainHomePresenterTarget? = null

    override fun init(target: MainHomePresenterTarget) {
        this.target = target
    }

    override fun setupUserInfo(forceRefresh: Boolean) {
        userUseCase.getUser(forceRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableApiSingleObserver<UserModel>() {

                    override fun onSuccess(userModel: UserModel) {
                        target?.bindUserInfo(userModel)
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(disposables)
    }

    override fun setupBalanceInfo(forceRefresh: Boolean) {
        userUseCase.getBalances(forceRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<BalancesModel>() {

                    override fun onSuccess(t: BalancesModel) {
                        target?.bindBalanceInfo(t)
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun setupUserTransactions(forceRefresh: Boolean) {
        userUseCase.getTopTransactionsByUserID(0, historyPageSize, forceRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserTransactionsModel>() {

                    override fun onSuccess(model: UserTransactionsModel) {
                        target?.bindUserTransactions(model, forceRefresh)
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit, forceRefresh: Boolean) {
        userUseCase.getTopTransactionsByUserID(lastUserTransactionID, historyPageSize, forceRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableApiSingleObserver<UserTransactionsModel>() {

                    override fun onSuccess(t: UserTransactionsModel) {
                        handleLoadedData(t)
                    }

                    override fun onOtherError(error: Throwable?) {
                        error?.let { target?.showError(error) }
                    }

                    override fun onMaintenance() {
                        target?.showMaintenance()
                    }

                }).addTo(this.disposables)
    }

    override fun onProfileAreaClicked() {
        target?.gotoProfileDetail()
    }

}
