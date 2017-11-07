package com.ttvnp.ttj_asset_android_client.data.entity

import io.realm.RealmObject
import java.util.*

open class DeviceEntity (
        open var accessToken: String = "",
        open var accessTokenExpiry: Date? = null,
        open var deviceToken: String = "",
        open var grantPushNotification: Boolean = false,
        open var grantEmailNotification: Boolean = false
) : RealmObject()
