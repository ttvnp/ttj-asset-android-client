package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainHomePresenter {
    fun init(target: MainHomePresenterTarget)
    fun setupUserInfo(forceRefresh: Boolean)
    fun setupBalanceInfo()
    fun setupUserTransactions()
    fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit)
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
                .subscribeWith(object : DisposableSingleObserver<UserModel>() {
                    override fun onSuccess(userModel: UserModel) {
                        target?.bindUserInfo(userModel)
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun setupBalanceInfo() {
        userUseCase.getBalances()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<BalancesModel>() {
                    override fun onSuccess(t: BalancesModel) {
                        target?.bindBalanceInfo(t)
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun setupUserTransactions() {
        userUseCase.getTopTransactionsByUserID(0, historyPageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserTransactionsModel>() {
                    override fun onSuccess(t: UserTransactionsModel) {
                        target?.bindUserTransactions(t)
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }

    override fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit) {
        userUseCase.getTopTransactionsByUserID(lastUserTransactionID, historyPageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserTransactionsModel>() {
                    override fun onSuccess(t: UserTransactionsModel) {
                        handleLoadedData(t)
                    }
                    override fun onError(e: Throwable) {
                        target?.showError(e)
                    }
                }).addTo(this.disposables)
    }
}
