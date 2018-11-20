package com.ttvnp.ttj_asset_android_client.presentation.ui.data

import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoStellarInfoModel
import java.io.Serializable

data class QRCodeInfoBridgeData(
        val emailAddress: String = "",
        val assetType: String = "",
        val amount: Long = 0L
) : Serializable

data class QRCodeInfoStellarBridgeData(
        val strAccountID: String = ""
) : Serializable

class QRCodeInfoBridgeDataTranslator {
    fun translate(model: QRCodeInfoModel): QRCodeInfoBridgeData {
        return QRCodeInfoBridgeData(
                emailAddress = model.emailAddress,
                assetType = model.assetType.rawValue,
                amount = model.amount
        )
    }

    fun translate(data: QRCodeInfoBridgeData): QRCodeInfoModel {
        return QRCodeInfoModel(
                emailAddress = data.emailAddress,
                assetType = AssetType.values().firstOrNull {
                    it.rawValue == data.assetType
                } ?: AssetType.ASSET_TYPE_POINT,
                amount = data.amount
        )
    }
}

class QRCodeInfoStellarBirdgeDataTranslator {
    fun translate(model: QRCodeInfoStellarInfoModel): QRCodeInfoStellarBridgeData {
        return QRCodeInfoStellarBridgeData(
                strAccountID = model.strAccountId
        )
    }

    fun translate(data: QRCodeInfoStellarBridgeData): QRCodeInfoStellarInfoModel {
        return QRCodeInfoStellarInfoModel(
                strAccountId = data.strAccountID
        )
    }
}