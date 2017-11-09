package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import java.util.*

@Table
class DeviceEntity (
        @Column @Setter("accessToken")
        val accessToken: String = "",

        @Column @Setter("accessTokenExpiry")
        val accessTokenExpiry: Date? = null,

        @Column @Setter("deviceToken")
        val deviceToken: String = "",

        @Column @Setter("grantPushNotification")
        val grantPushNotification: Boolean = false,

        @Column @Setter("grantEmailNotification")
        val grantEmailNotification: Boolean = false
)
