package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import javax.inject.Inject

interface UserDataStore {
    fun update(entity: UserEntity): UserEntity
}

class UserDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : UserDataStore {

    override fun update(entity: UserEntity): UserEntity {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            orma.deleteFromUserEntity().execute()
            orma.insertIntoUserEntity(entity)
        }
        return entity
    }
}