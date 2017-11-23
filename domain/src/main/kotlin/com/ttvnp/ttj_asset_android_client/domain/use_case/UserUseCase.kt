package com.ttvnp.ttj_asset_android_client.domain.use_case

import android.util.Log
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserTransactionRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import java.io.File
import javax.inject.Inject

interface UserUseCase {

    fun getUser(forceRefresh: Boolean): Single<UserModel>

    fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String): Single<ModelWrapper<UserModel?>>

    fun getTargetUser(emailAddress: String): Single<OtherUserModel>

    fun getBalances(): Single<BalancesModel>

    fun getTopTransactionsByUserID(upperID: Long, limit: Long): Single<UserTransactionsModel>

    fun createTransaction(sendInfoModel: SendInfoModel): Single<UserTransactionModel>
}

class UserUseCaseImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val balanceRepository: BalanceRepository,
        private val userTransactionRepository: UserTransactionRepository
) : UserUseCase {

    override fun getUser(forceRefresh: Boolean): Single<UserModel> {
        return userRepository.getUser(forceRefresh)
    }

    override fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String): Single<ModelWrapper<UserModel?>> {
        return userRepository.updateUser(profileImageFile, firstName, middleName, lastName, address)
    }

    override fun getTargetUser(emailAddress: String): Single<OtherUserModel> {
        return userRepository.getTargetUser(emailAddress)
    }

    override fun getBalances(): Single<BalancesModel> {
        return balanceRepository.getBalances()
    }

    override fun getTopTransactionsByUserID(upperID: Long, limit: Long): Single<UserTransactionsModel> {
        return userTransactionRepository.getTopByUserID(upperID, limit)
    }

    override fun createTransaction(sendInfoModel: SendInfoModel): Single<UserTransactionModel> {
        val disposables = CompositeDisposable()
        return userTransactionRepository.createTransaction(sendInfoModel, { balanceBodels ->
            this.balanceRepository.updateBalances(balanceBodels).subscribeWith(object : DisposableSingleObserver<BalancesModel>() {
                override fun onSuccess(t: BalancesModel) {
                    Log.d("updateBalances", "balance update was successfully executed after transaction :)")
                }
                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            }).addTo(disposables)
        })
    }
}
