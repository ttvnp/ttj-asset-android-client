package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.StellarAccountEntity
import javax.inject.Inject

interface StellarAccountDataStore {
    fun get(): StellarAccountEntity?
    fun update(entity: StellarAccountEntity): StellarAccountEntity
}

class StellarAccountDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : StellarAccountDataStore {

    override fun get(): StellarAccountEntity? {
        val orma = ormaHolder.ormaDatabase
        return orma.selectFromStellarAccountEntity().firstOrNull()
    }

    override fun update(entity: StellarAccountEntity): StellarAccountEntity {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            orma.deleteFromStellarAccountEntity().execute()
            orma.insertIntoStellarAccountEntity(entity)
        }
        return entity
    }
}