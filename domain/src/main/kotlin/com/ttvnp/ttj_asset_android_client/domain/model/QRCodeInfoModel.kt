package com.ttvnp.ttj_asset_android_client.domain.model

class QRCodeInfoModel(
        val emailAddress: String = "",
        val assetType: AssetType = AssetType.ASSET_TYPE_POINT,
        val amount: Long = 0L
) {
    companion object {
        fun load(qrCodeString: String): QRCodeInfoModel {
            val info = qrCodeString.split(";")
            return QRCodeInfoModel(
                    emailAddress = info[0],
                    assetType = if (1 < info.size) AssetType.values().firstOrNull {
                        info[1] == it.rawValue
                    }?:AssetType.ASSET_TYPE_POINT else AssetType.ASSET_TYPE_POINT,
                    amount = if (2 < info.size) info[2].toLong() else 0L
            )
        }
    }


    fun toQRString(): String {
        return "%s;%s;%s".format(emailAddress, assetType.rawValue, amount)
    }
}