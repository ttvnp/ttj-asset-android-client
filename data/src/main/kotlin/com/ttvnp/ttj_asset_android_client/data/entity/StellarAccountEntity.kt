package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class StellarAccountEntity(
    @Column(defaultExpr = "")
    @Setter("strAccountID")
    var strAccountID: String = "",

    @Column(defaultExpr = "")
    @Setter("strDepositMemoText")
    var strDepositMemoText: String = "",

    @Column(defaultExpr = "now") @Setter("updatedAt")
    var updatedAt: Date = Now()
)