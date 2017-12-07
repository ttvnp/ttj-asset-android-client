package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.AppEntity
import javax.inject.Inject

interface AppDataStore {
    fun get(): AppEntity
    fun updateLoadPaymentHistory(): AppEntity
}

class AppDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : AppDataStore {

    private var cached: AppEntity? = null

    override fun get(): AppEntity {
        if (cached != null) return cached!!
        val result = ormaHolder.ormaDatabase.selectFromAppEntity().firstOrNull()
        return result?: AppEntity(loadPaymentHistory = false)
    }

    override fun updateLoadPaymentHistory(): AppEntity {
        val orma = ormaHolder.ormaDatabase
        val entity = get().copy(loadPaymentHistory = true)
        orma.transactionSync {
            orma.deleteFromAppEntity().execute()
            orma.insertIntoAppEntity(entity)
        }
        cached = entity
        return entity
    }
}