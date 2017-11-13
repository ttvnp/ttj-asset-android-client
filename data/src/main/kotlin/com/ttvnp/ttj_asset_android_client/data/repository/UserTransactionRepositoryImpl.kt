package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.UserTransactionEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.UserTransactionDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.UserTransactionTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionsModel
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
}