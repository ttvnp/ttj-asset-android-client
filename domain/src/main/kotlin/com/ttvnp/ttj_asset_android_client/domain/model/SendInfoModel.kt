package com.ttvnp.ttj_asset_android_client.domain.model

class SendInfoModel(
        val targetUserID: Long = 0L,
        val targetUserEmailAddress: String = "",
        val targetUserProfileImageID: Long = 0L,
        val targetUserProfileImageURL: String = "",
        val targetUserFirstName: String = "",
        val targetUserMiddleName: String = "",
        val targetUserLastName: String = "",
        val targetUserStrAccountID: String = "",
        val targetUserStrMemoText: String = "",
        val assetType: AssetType = AssetType.ASSET_TYPE_POINT,
        val amount: Long = 0L
) : BaseModel()
