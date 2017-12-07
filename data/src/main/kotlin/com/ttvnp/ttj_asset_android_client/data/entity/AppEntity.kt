package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
data class AppEntity (
        @Column @Setter("loadPaymentHistory")
        val loadPaymentHistory: Boolean = false
)
