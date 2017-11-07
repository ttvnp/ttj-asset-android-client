package com.ttvnp.ttj_asset_android_client.data.driver

import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject

interface RealmDriver {
    fun getRealm(): Realm
}

class RealmDriverImpl @Inject constructor(context: Context) : RealmDriver {

    private val realm: Realm

    init {
        Realm.init(context)
        val config: RealmConfiguration = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
    }

    override fun getRealm() : Realm = realm
}
