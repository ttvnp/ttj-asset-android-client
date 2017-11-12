package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface MainHomePresenter {
    fun init(target: MainHomePresenterTarget)
    fun setupUserInfo()
    fun setupBalanceInfo()
    fun setupUserTransactions()
    fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit)
}

class MainHomePresenterImpl @Inject constructor(val userUseCase: UserUseCase) : BasePresenter(), MainHomePresenter {

    private var target: MainHomePresenterTarget? = null

    override fun init(target: MainHomePresenterTarget) {
        this.target = target
    }

    override fun setupUserInfo() {
        userUseCase.getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UserModel>() {
                    override fun onSuccess(t: UserModel) {
                        target?.bindUserInfo(t)
                    }
                    override fun onError(e: Throwable) {
                        // do nothing...
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
                        // do nothing...
                    }
                }).addTo(this.disposables)
    }

    override fun setupUserTransactions() {
        // TODO it's just a dummy.
        val userTransactions = arrayListOf<UserTransactionModel>()
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "test@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.RECEIVE,
                targetUserID = 3L,
                targetUserEmailAddress = "hoge@test.com",
                targetUserProfileImageID = 3,
                targetUserProfileImageURL = "https://qiita-image-store.s3.amazonaws.com/0/188896/profile-images/1498382989",
                targetUserFirstName = "Hiroki",
                targetUserMiddleName = "",
                targetUserLastName = "Tanaka",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "3@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "4@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "5@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "6@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "7@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "8@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "9@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "10@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "11@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "12@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "13@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "14@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 100L
        ))
        val model = UserTransactionsModel(userTransactions, true)
        target?.bindUserTransactions(model)
    }

    override fun loadMoreUserTransactions(lastUserTransactionID: Long, handleLoadedData: (UserTransactionsModel) -> Unit) {
        // TODO it's just a dummy.
        val userTransactions = arrayListOf<UserTransactionModel>()
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "loadmore1@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_COIN,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.RECEIVE,
                targetUserID = 3L,
                targetUserEmailAddress = "hoge@test.com",
                targetUserProfileImageID = 3,
                targetUserProfileImageURL = "https://qiita-image-store.s3.amazonaws.com/0/188896/profile-images/1498382989",
                targetUserFirstName = "Hiroki",
                targetUserMiddleName = "Loaded",
                targetUserLastName = "Tanaka",
                assetType = AssetType.ASSET_TYPE_COIN,
                amount = 100L
        ))
        userTransactions.add(UserTransactionModel(
                id = 1L,
                loggedAt = Now(),
                transactionType = TransactionType.SEND,
                targetUserID = 2L,
                targetUserEmailAddress = "loadmore3@test.com",
                targetUserProfileImageID = 0,
                targetUserProfileImageURL = "",
                targetUserFirstName = "",
                targetUserMiddleName = "",
                targetUserLastName = "",
                assetType = AssetType.ASSET_TYPE_COIN,
                amount = 100L
        ))
        val model = UserTransactionsModel(userTransactions, false)
        handleLoadedData(model)
    }
}
