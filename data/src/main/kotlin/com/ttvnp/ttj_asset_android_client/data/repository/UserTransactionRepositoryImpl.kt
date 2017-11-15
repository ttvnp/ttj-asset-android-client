package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserTransactionEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.UserTransactionDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.BalanceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTransactionTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.UserTransactionRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import io.reactivex.Single
import javax.inject.Inject

class UserTransactionRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val userTransactionDataStore : UserTransactionDataStore
) : UserTransactionRepository {

    override fun getTopByUserID(upperID: Long, limit: Long): Single<UserTransactionsModel> {
        return userService.getTransactions(upperID).map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
            var userTransactionEntities: Collection<UserTransactionEntity> = arrayListOf()
            userTransactionEntities = userTransactionEntities.toMutableList()
            for (r in response.userTransactions) {
                userTransactionEntities.add(UserTransactionEntity(
                        id = r.id,
                        loggedAt = r.loggedAt?:Now(),
                        transactionType = r.transactionType,
                        targetUserID = r.targetUserID,
                        targetUserEmailAddress = r.targetUserEmailAddress,
                        targetUserProfileImageID = r.targetUserProfileImageID,
                        targetUserProfileImageURL = r.targetUserProfileImageURL,
                        targetUserFirstName = r.targetUserFirstName,
                        targetUserMiddleName = r.targetUserMiddleName,
                        targetUserLastName = r.targetUserLastName,
                        assetType = r.assetType,
                        amount = r.amount
                ))
            }
            userTransactionEntities = userTransactionDataStore.updates(userTransactionEntities)
            UserTransactionTranslator().translateUserTransactions(userTransactionEntities, response.hasMore)
        }.onErrorReturn { e ->
            var userTransactionEntities = userTransactionDataStore.getTopByUserID(upperID, limit+1)
            val hasMore = limit < userTransactionEntities.size
            if (hasMore) {
                userTransactionEntities = userTransactionEntities.filter {
                    it.id != userTransactionEntities.last().id
                }
            }
            UserTransactionTranslator().translateUserTransactions(userTransactionEntities, hasMore)
        }
    }

    override fun createTransaction(sendInfoModel: SendInfoModel, onReceiveBalances: (Collection<BalanceModel>) -> Unit): Single<UserTransactionModel> {
        return userService.createTransaction(
                sendInfoModel.targetUserEmailAddress,
                sendInfoModel.assetType.rawValue,
                sendInfoModel.amount
        ).map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }

            // handle user transactions
            var userTransactionEntity = UserTransactionEntity(
                    id = response.userTransaction.id,
                    loggedAt = response.userTransaction.loggedAt?:Now(),
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

            UserTransactionTranslator().translate(userTransactionEntity)!!
        }
    }
}