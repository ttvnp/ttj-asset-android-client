package com.ttvnp.ttj_asset_android_client.data.store

import com.ttvnp.ttj_asset_android_client.data.driver.RealmDriver
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import javax.inject.Inject

interface DeviceDataStore {
    fun get(): DeviceEntity?
    fun update(entity: DeviceEntity): DeviceEntity
}

class DeviceDataStoreImpl @Inject constructor(val realmDriver: RealmDriver) : DeviceDataStore {

    override fun get(): DeviceEntity? {
        val realm = realmDriver.getRealm()
        return realm.where(DeviceEntity::class.java).findAll().firstOrNull()
    }

    override fun update(entity: DeviceEntity): DeviceEntity {
        val realm = realmDriver.getRealm()
        realm.executeTransaction {
            realm.where(DeviceEntity::class.java).findAll().deleteAllFromRealm()
            realm.copyToRealm(entity)
        }
        return entity
    }
}
