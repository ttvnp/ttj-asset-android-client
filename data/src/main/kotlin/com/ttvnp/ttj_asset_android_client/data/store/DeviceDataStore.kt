package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.OrmaHolder
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import javax.inject.Inject

interface DeviceDataStore {
    fun get(): DeviceEntity?
    fun update(entity: DeviceEntity): DeviceEntity
    fun removeAll()
}

class DeviceDataStoreImpl @Inject constructor(val ormaHolder: OrmaHolder) : DeviceDataStore {

    override fun get(): DeviceEntity? {
        return ormaHolder.ormaDatabase.selectFromDeviceEntity().firstOrNull()
    }

    override fun update(entity: DeviceEntity): DeviceEntity {
        val orma = ormaHolder.ormaDatabase
        orma.transactionSync {
            orma.deleteFromDeviceEntity().execute()
            orma.insertIntoDeviceEntity(entity)
        }
        return entity
    }

    override fun removeAll() {
        ormaHolder.ormaDatabase.deleteAll()
    }
}
