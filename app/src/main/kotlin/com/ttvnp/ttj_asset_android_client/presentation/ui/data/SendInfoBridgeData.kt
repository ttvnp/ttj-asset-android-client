package com.ttvnp.ttj_asset_android_client.presentation.ui.data

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import java.io.Serializable

data class SendInfoBridgeData(
        val targetUserID: Long = 0L,
        val targetUserEmailAddress: String = "",
        val targetUserProfileImageID: Long = 0L,
        val targetUserProfileImageURL: String = "",
        val targetUserFirstName: String = "",
        val targetUserMiddleName: String = "",
        val targetUserLastName: String = "",
        val assetType: String = "",
        val amount: Long = 0L
) : Serializable

class SendInfoBridgeDataTranslator {
    fun translate(model: SendInfoModel): SendInfoBridgeData {
        return SendInfoBridgeData(
                targetUserID = model.targetUserID,
                targetUserEmailAddress = model.targetUserEmailAddress,
                targetUserProfileImageID = model.targetUserProfileImageID,
                targetUserProfileImageURL = model.targetUserProfileImageURL,
                targetUserFirstName = model.targetUserFirstName,
                targetUserMiddleName = model.targetUserMiddleName,
                targetUserLastName = model.targetUserLastName,
                assetType = model.assetType.rawValue,
                amount = model.amount
        )
    }
    fun translate(data: SendInfoBridgeData): SendInfoModel {
        return SendInfoModel(
                targetUserID = data.targetUserID,
                targetUserEmailAddress = data.targetUserEmailAddress,
                targetUserProfileImageID = data.targetUserProfileImageID,
                targetUserProfileImageURL = data.targetUserProfileImageURL,
                targetUserFirstName = data.targetUserFirstName,
                targetUserMiddleName = data.targetUserMiddleName,
                targetUserLastName = data.targetUserLastName,
                assetType = AssetType.values().firstOrNull {
                    it.rawValue == data.assetType
                }?:AssetType.ASSET_TYPE_POINT,
                amount = data.amount
        )
    }
}