package com.ttvnp.ttj_asset_android_client.domain.model

class QRCodeInfoModel(
        val emailAddress: String = "",
        val assetType: AssetType = AssetType.ASSET_TYPE_POINT,
        val amount: Long = 0L
) {
    fun toQRString(): String {
        return "%s;%s;%s".format(emailAddress, assetType.rawValue, amount)
    }
}