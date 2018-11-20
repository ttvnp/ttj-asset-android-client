package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class UserTransactionEntity(

        @PrimaryKey(auto = false, autoincrement = false)
        @Setter("id")
        val id: Long = 0L,

        @Column
        @Setter("loggedAt")
        val loggedAt: Date = Now(),

        @Column(defaultExpr = "0")
        @Setter("transactionStatus")
        val transactionStatus: Int = 0,

        @Column(defaultExpr = "0")
        @Setter("transactionType")
        val transactionType: Int = 0,

        @Column
        @Setter("targetUserID")
        val targetUserID: Long = 0L,

        @Column
        @Setter("targetUserEmailAddress")
        val targetUserEmailAddress: String = "",

        @Column
        @Setter("targetUserProfileImageID")
        val targetUserProfileImageID: Long = 0L,

        @Column
        @Setter("targetUserProfileImageURL")
        val targetUserProfileImageURL: String = "",

        @Column
        @Setter("targetUserFirstName")
        val targetUserFirstName: String = "",

        @Column
        @Setter("targetUserMiddleName")
        val targetUserMiddleName: String = "",

        @Column
        @Setter("targetUserLastName")
        val targetUserLastName: String = "",

        @Column
        @Setter("targetStrAccountID")
        val targetStrAccountID: String = "",

        @Column
        @Setter("targetMemoText")
        val targetMemoText: String = "",

        @Column
        @Setter("assetType")
        val assetType: String = "",

        @Column
        @Setter("amount")
        val amount: Long = 0L
)