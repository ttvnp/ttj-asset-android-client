package com.ttvnp.ttj_asset_android_client.domain.model

import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

class UserTransactionModel (
        val id: Long = 0L,
        val loggedAt: Date = Now(),
        val transactionType: TransactionType = TransactionType.SEND,
        val targetUserID: Long = 0L,
        val targetUserEmailAddress: String = "",
        val targetUserProfileImageID: Long = 0L,
        val targetUserProfileImageURL: String = "",
        val targetUserFirstName: String = "",
        val targetUserMiddleName: String = "",
        val targetUserLastName: String = "",
        val assetType: AssetType = AssetType.ASSET_TYPE_POINT,
        val amount: Long = 0L
) : BaseModel()
