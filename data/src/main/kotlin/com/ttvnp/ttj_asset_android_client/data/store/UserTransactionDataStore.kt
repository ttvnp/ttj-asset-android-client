package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.UserTransactionEntity
import javax.inject.Inject

interface UserTransactionDataStore {
    fun getTopByUserID(upperID: Long, limit: Long): Collection<UserTransactionEntity>
    fun upsert(userTransaction: UserTransactionEntity): UserTransactionEntity
    fun updates(userTransactions: Collection<UserTransactionEntity>): Collection<UserTransactionEntity>
}

class UserTransactionDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : UserTransactionDataStore {

    override fun getTopByUserID(upperID: Long, limit: Long): Collection<UserTransactionEntity> {
        val orma = ormaHolder.ormaDatabase
        return orma.selectFromUserTransactionEntity().idLt(upperID).limit(limit).toList()
    }

    override fun upsert(userTransaction: UserTransactionEntity): UserTransactionEntity {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            orma.relationOfUserTransactionEntity().upsert(userTransaction)
        }
        return userTransaction
    }

    override fun updates(userTransactions: Collection<UserTransactionEntity>): Collection<UserTransactionEntity> {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            for (ut in userTransactions) {
                orma.relationOfUserTransactionEntity().upsert(ut)
            }
        }
        return userTransactions
    }
}