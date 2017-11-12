package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import javax.inject.Inject

interface BalanceDataStore {
    fun getAll(): Collection<BalanceEntity>
    fun updates(balances: Collection<BalanceEntity>): Collection<BalanceEntity>
}

class BalanceDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : BalanceDataStore {

    override fun getAll(): Collection<BalanceEntity> {
        val orma = ormaHolder.ormaDatabase
        return orma.selectFromBalanceEntity().toList()
    }

    override fun updates(balances: Collection<BalanceEntity>): Collection<BalanceEntity> {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            for (b in balances) {
                orma.relationOfBalanceEntity().upsert(b)
            }
        }
        return balances
    }
}