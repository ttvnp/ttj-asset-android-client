package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserTransactionRepository
import com.ttvnp.ttj_asset_android_client.domain.util.isEmailValid
import com.ttvnp.ttj_asset_android_client.domain.util.isValidAmount
import com.ttvnp.ttj_asset_android_client.domain.util.toAmount
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import java.io.File
import javax.inject.Inject

interface UserUseCase {

    fun getUser(forceRefresh: Boolean): Single<UserModel>

    fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String, genderType: Int, dob: String, cellphoneNumberNationalCode: String, cellphoneNumber: String): Single<ModelWrapper<UserModel?>>

    fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?): Single<ModelWrapper<UserModel?>>

    fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>>

    fun getBalances(forceRefresh: Boolean): Single<BalancesModel>

    fun checkSendAmount(assetType: AssetType, amountString: String): Single<ErrorCode>

    fun getTopTransactionsByUserID(upperID: Long, limit: Long, forceRefresh: Boolean): Single<UserTransactionsModel>

    fun createTransaction(sendInfoModel: SendInfoModel): Single<ModelWrapper<UserTransactionModel?>>

    fun changePassword(oldPassword: String, newPassword: String, retypePassword: String): Single<ModelWrapper<UserModel?>>
}

class UserUseCaseImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val balanceRepository: BalanceRepository,
        private val userTransactionRepository: UserTransactionRepository
) : UserUseCase {

    override fun getUser(forceRefresh: Boolean): Single<UserModel> {
        return userRepository.getUser(forceRefresh)
    }

    override fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String, genderType: Int, dob: String, cellphoneNumberNationalCode: String, cellphoneNumber: String): Single<ModelWrapper<UserModel?>> {
        return userRepository.updateUser(profileImageFile, firstName, middleName, lastName, address, genderType, dob, cellphoneNumberNationalCode, cellphoneNumber)
    }

    override fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?): Single<ModelWrapper<UserModel?>> {
        return userRepository.uploadIdDocument(faceImageFile, addressImageFile)
    }

    override fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>> {
        val input = emailAddress.trim()
        if (!isEmailValid(input)) {
            return Single.create { subscriber ->
                subscriber.onSuccess(ModelWrapper<OtherUserModel?>(null, ErrorCode.ERROR_VALIDATION_EMAIL))
            }
        }
        return userRepository.getTargetUser(emailAddress)
    }

    override fun getBalances(forceRefresh: Boolean): Single<BalancesModel> {
        return balanceRepository.getBalances(forceRefresh)
    }

    override fun checkSendAmount(assetType: AssetType, amountString: String): Single<ErrorCode> {
        return Single.create { subscriber ->
            if (!amountString.isValidAmount()) {
                subscriber.onSuccess(ErrorCode.ERROR_VALIDATION_AMOUNT_LONG)
                return@create
            }
            val disposables = CompositeDisposable()
            balanceRepository.getBalances(true).subscribeWith(object : DisposableSingleObserver<BalancesModel>() {
                override fun onSuccess(balances: BalancesModel) {
                    val afordable = if (assetType == AssetType.ASSET_TYPE_COIN) balances.coinBalance.amount else balances.pointBalance.amount
                    if (afordable < amountString.toAmount()) {
                        subscriber.onSuccess(ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT)
                    } else {
                        subscriber.onSuccess(ErrorCode.NO_ERROR)
                    }
                }

                override fun onError(e: Throwable) {
                    subscriber.onError(e)
                }
            }).addTo(disposables)
        }
    }

    override fun getTopTransactionsByUserID(upperID: Long, limit: Long, forceRefresh: Boolean): Single<UserTransactionsModel> {
        return userTransactionRepository.getTopByUserID(upperID, limit, forceRefresh)
    }

    override fun createTransaction(sendInfoModel: SendInfoModel): Single<ModelWrapper<UserTransactionModel?>> {
        return userTransactionRepository.createTransaction(sendInfoModel, { balanceModels ->
            val disposables = CompositeDisposable()
            this.balanceRepository.updateBalances(balanceModels).subscribeWith(object : DisposableSingleObserver<BalancesModel>() {
                override fun onSuccess(t: BalancesModel) {
                }

                override fun onError(e: Throwable) {
                }
            }).addTo(disposables)
        })
    }

    override fun changePassword(oldPassword: String, newPassword: String, retypePassword: String): Single<ModelWrapper<UserModel?>> {
        return userRepository.changePassword(oldPassword, newPassword, retypePassword)
    }
}
