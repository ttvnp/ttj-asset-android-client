package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class BalanceEntity (
        @PrimaryKey @Setter("assetType")
        val assetType: String = "",

        @Column @Setter("amount")
        val amount: Long = 0L,

        @Column(defaultExpr = "now") @Setter("updatedAt")
        var updatedAt: Date = Now()
)