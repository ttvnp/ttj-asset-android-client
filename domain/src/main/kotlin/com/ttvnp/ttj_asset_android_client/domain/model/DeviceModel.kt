package com.ttvnp.ttj_asset_android_client.domain.model

import java.util.Date

class DeviceModel(
        val accessToken: String = "",
        val accessTokenExpiry: Date = Date(),
        val isActivated: Boolean = false,
        val deviceToken: String = "",
        val grantPushNotification: Boolean = false,
        val grantEmailNotification: Boolean = false
) : BaseModel()
