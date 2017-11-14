package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import javax.inject.Inject

interface OtherUserDataStore {
    fun getByEmailAddress(emailAddress: String): OtherUserEntity?
    fun update(entity: OtherUserEntity): OtherUserEntity
}

class OtherUserDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : OtherUserDataStore {

    override fun getByEmailAddress(emailAddress: String): OtherUserEntity? {
        val orma = ormaHolder.ormaDatabase
        return orma.selectFromOtherUserEntity().emailAddressEq(emailAddress).firstOrNull()
    }

    override fun update(entity: OtherUserEntity): OtherUserEntity {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            orma.relationOfOtherUserEntity().upsert(entity)
        }
        return entity
    }
}
