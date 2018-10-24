package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserTransactionEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.service.response.ServiceErrorCode
import com.ttvnp.ttj_asset_android_client.data.store.AppDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserTransactionDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.BalanceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTransactionTranslator
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.UserTransactionRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import io.reactivex.Single
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserTransactionRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val userTransactionDataStore : UserTransactionDataStore,
        private val appDataStore: AppDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore
) : UserTransactionRepository {

    override fun getTopByUserID(upperID: Long, limit: Long, forceRefresh: Boolean): Single<UserTransactionsModel> {
        return Single.create { subscriber ->
            var userTransactionsModel: UserTransactionsModel? = null
            val getModelFromLocalDatabase: () -> UserTransactionsModel = {
                var localEntities = userTransactionDataStore.getTopByUserID(upperID, limit+1)
                val hasMore = limit < localEntities.size
                if (hasMore) {
                    localEntities = localEntities.filter {
                        it.id != localEntities.last().id
                    }
                }
                UserTransactionTranslator().translateUserTransactions(localEntities, hasMore)
            }
            var refresh = forceRefresh
            if (!refresh) {
                val appEntity = appDataStore.get()
                if (appEntity.loadPaymentHistory) {
                    userTransactionsModel = getModelFromLocalDatabase()
                    val containsPending = userTransactionsModel.userTransactions.filter { mdl ->
                        mdl.transactionStatus == TransactionStatus.Unprocessed
                    }.firstOrNull() != null
                    if (containsPending) {
                        refresh = true
                    }
                } else {
                    refresh = true
                }
            }
            if (refresh) {
                try {
                    val transactionResponse = userService.getTransactions(upperID).execute()
                    if (!transactionResponse.isSuccessful) {
                        subscriber.onError(HttpException(transactionResponse))
                        return@create
                    }
                    transactionResponse.body()?.let { response ->
                        if (response.hasError()) {
                            return@let
                        }
                        var entities: Collection<UserTransactionEntity> = arrayListOf()
                        entities = entities.toMutableList()
                        for (r in response.userTransactions) {
                            entities.add(UserTransactionEntity(
                                    id = r.id,
                                    loggedAt = r.loggedAt?:Now(),
                                    transactionStatus = r.transactionStatus,
                                    transactionType = r.transactionType,
                                    targetUserID = r.targetUserID,
                                    targetUserEmailAddress = r.targetUserEmailAddress,
                                    targetUserProfileImageID = r.targetUserProfileImageID,
                                    targetUserProfileImageURL = r.targetUserProfileImageURL,
                                    targetUserFirstName = r.targetUserFirstName,
                                    targetUserMiddleName = r.targetUserMiddleName,
                                    targetUserLastName = r.targetUserLastName,
                                    targetStrAccountID = r.targetStrAccountID,
                                    targetMemoText = r.strMemoText,
                                    assetType = r.assetType,
                                    amount = r.amount
                            ))
                        }
                        entities = userTransactionDataStore.updates(entities)
                        appDataStore.get().let { app ->
                            if (!app.loadPaymentHistory) {
                                appDataStore.updateLoadPaymentHistory()
                            }
                        }
                        userTransactionsModel = UserTransactionTranslator().translateUserTransactions(entities, response.hasMore)
                    }
                } catch (e: IOException) {
                    // ignore connection exception.
                }
            }
            if (userTransactionsModel == null) {
                userTransactionsModel = getModelFromLocalDatabase()
            }
            subscriber.onSuccess(userTransactionsModel!!)
        }
    }

    override fun createTransaction(sendInfoModel: SendInfoModel,
                                   password: String,
                                   onReceiveBalances: (Collection<BalanceModel>) -> Unit
    ): Single<ModelWrapper<UserTransactionModel?>> {

        return Single.create { subscriber ->
            try {
                val deviceInfo = deviceInfoDataStore.get()
                val createTransactionResponse = userService.createTransaction(
                        deviceInfo?.credential?: "",
                        sendInfoModel.targetUserEmailAddress,
                        sendInfoModel.assetType.rawValue,
                        sendInfoModel.amount,
                        password
                ).execute()
                if (!createTransactionResponse.isSuccessful) {
                    subscriber.onError(HttpException(createTransactionResponse))
                    return@create
                }
                createTransactionResponse.body()?.let {
                    val response = it
                    if (response.hasError()) {
                        val errorCode: ErrorCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT.rawValue -> ErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT
                            ServiceErrorCode.ERROR_DATA_NOT_FOUND.rawValue -> ErrorCode.ERROR_CANNOT_FIND_TARGET_USER
                            ServiceErrorCode.ERROR_LOCKED_OUT.rawValue -> ErrorCode.ERROR_LOCKED_OUT
                            ServiceErrorCode.ERROR_TOO_MUCH_AMOUNT.rawValue -> ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        subscriber.onSuccess(ModelWrapper(null, errorCode))
                        return@create
                    }
                    // handle user transactions
                    var userTransactionEntity = UserTransactionEntity(
                            id = response.userTransaction.id,
                            loggedAt = response.userTransaction.loggedAt?:Now(),
                            transactionStatus = response.userTransaction.transactionStatus,
                            transactionType = response.userTransaction.transactionType,
                            targetUserID = response.userTransaction.targetUserID,
                            targetUserEmailAddress = response.userTransaction.targetUserEmailAddress,
                            targetUserProfileImageID = response.userTransaction.targetUserProfileImageID,
                            targetUserProfileImageURL = response.userTransaction.targetUserProfileImageURL,
                            targetUserFirstName = response.userTransaction.targetUserFirstName,
                            targetUserMiddleName = response.userTransaction.targetUserMiddleName,
                            targetUserLastName = response.userTransaction.targetUserLastName,
                            assetType = response.userTransaction.assetType,
                            amount = response.userTransaction.amount
                    )
                    userTransactionEntity = userTransactionDataStore.upsert(userTransactionEntity)

                    // handle balances
                    var balanceEntities: Collection<BalanceEntity> = arrayListOf()
                    balanceEntities = balanceEntities.toMutableList()
                    for (r in response.balances) {
                        balanceEntities.add(BalanceEntity(
                                assetType = r.assetType,
                                amount = r.amount
                        ))
                    }
                    val balanceModels = BalanceTranslator().translate(balanceEntities)
                    onReceiveBalances(balanceModels)
                    val model = UserTransactionTranslator().translate(userTransactionEntity)!!
                    subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
                return@create
            }
        }
    }

    override fun createExternalTransaction(
            strAccountID: String,
            memoText: String,
            assetType: AssetType,
            amount: Long,
            password: String,
            onReceiveBalances: (Collection<BalanceModel>) -> Unit
    ): Single<ModelWrapper<UserTransactionModel?>> {
        return Single.create { subscriber ->
            try {
                val deviceInfo = deviceInfoDataStore.get()
                val createExternalTransactionResponse = userService.createExternalTransaction(
                        deviceInfo?.credential?: "",
                        strAccountID,
                        memoText,
                        assetType.rawValue,
                        amount,
                        password
                ).execute()
                if (!createExternalTransactionResponse.isSuccessful) {
                    subscriber.onError(HttpException(createExternalTransactionResponse))
                    return@create
                }
                createExternalTransactionResponse.body()?.let { response ->
                    if (response.hasError()) {
                        return@let
                    }
                    createExternalTransactionResponse.body()?.let {
                        if (it.hasError()) {
                            val errorCode: ErrorCode = when (it.errorCode) {
                                ServiceErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT.rawValue -> ErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT
                                ServiceErrorCode.ERROR_DATA_NOT_FOUND.rawValue -> ErrorCode.ERROR_CANNOT_FIND_TARGET_USER
                                ServiceErrorCode.ERROR_LOCKED_OUT.rawValue -> ErrorCode.ERROR_LOCKED_OUT
                                ServiceErrorCode.ERROR_TOO_MUCH_AMOUNT.rawValue -> ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT
                                else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                            }
                            subscriber.onSuccess(ModelWrapper(null, errorCode))
                            return@create
                        }
                        // handle user transactions
                        var userTransactionEntity = UserTransactionEntity(
                                id = response.userTransaction.id,
                                loggedAt = response.userTransaction.loggedAt?:Now(),
                                transactionStatus = response.userTransaction.transactionStatus,
                                transactionType = response.userTransaction.transactionType,
                                targetUserID = response.userTransaction.targetUserID,
                                targetUserEmailAddress = response.userTransaction.targetUserEmailAddress,
                                targetUserProfileImageID = response.userTransaction.targetUserProfileImageID,
                                targetUserProfileImageURL = response.userTransaction.targetUserProfileImageURL,
                                targetUserFirstName = response.userTransaction.targetUserFirstName,
                                targetUserMiddleName = response.userTransaction.targetUserMiddleName,
                                targetUserLastName = response.userTransaction.targetUserLastName,
                                targetStrAccountID =  response.userTransaction.targetStrAccountID,
                                targetMemoText = response.userTransaction.strMemoText,
                                assetType = response.userTransaction.assetType,
                                amount = response.userTransaction.amount
                        )
                        userTransactionEntity = userTransactionDataStore.upsert(userTransactionEntity)

                        // handle balances
                        var balanceEntities: Collection<BalanceEntity> = arrayListOf()
                        balanceEntities = balanceEntities.toMutableList()
                        for (r in response.balances) {
                            balanceEntities.add(BalanceEntity(
                                    assetType = r.assetType,
                                    amount = r.amount
                            ))
                        }
                        val balanceModels = BalanceTranslator().translate(balanceEntities)
                        onReceiveBalances(balanceModels)
                        val model = UserTransactionTranslator().translate(userTransactionEntity)!!
                        subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
                    }
                }
            } catch (ex: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
                return@create
            }
        }
    }
}