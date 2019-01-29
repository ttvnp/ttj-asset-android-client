package com.ttvnp.ttj_asset_android_client.domain.model

class QRCodeInfoModel(
        val qrCodeType: QRCodeType = QRCodeType.BY_EMAIL,
        val emailAddress: String = "",
        val assetType: AssetType = AssetType.ASSET_TYPE_POINT,
        val amount: Long = 0L
) {
    companion object {
        fun load(qrCodeString: String): QRCodeInfoModel {
            val info = qrCodeString.split(";")
            return QRCodeInfoModel(
                    qrCodeType = if (info.isNotEmpty()) QRCodeType.values().firstOrNull {
                        info[0] == it.rawValue
                    } ?: QRCodeType.BY_EMAIL else QRCodeType.BY_STELLAR_ACCOUNT,
                    emailAddress = if (1 < info.size) info[1] else "",
                    assetType = if (2 < info.size) AssetType.values().firstOrNull {
                        info[2] == it.rawValue
                    } ?: AssetType.ASSET_TYPE_POINT else AssetType.ASSET_TYPE_POINT,
                    amount = if (3 < info.size) (info[3].toLongOrNull() ?: 0L) else 0L
            )
        }
    }

    fun toQRString(): String {
        return "%s;%s;%s;%s".format(qrCodeType.rawValue, emailAddress, assetType.rawValue, amount)
    }

}