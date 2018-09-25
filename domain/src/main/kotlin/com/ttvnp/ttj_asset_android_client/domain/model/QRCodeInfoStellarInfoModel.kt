package com.ttvnp.ttj_asset_android_client.domain.model

class QRCodeInfoStellarInfoModel(
    val qrCodeType: QRCodeType = QRCodeType.BY_STELLAR_ACCOUNT,
    val strAccountId: String = ""
) {
    companion object {
        fun load(qrCodeString: String): QRCodeInfoStellarInfoModel {
            val info = qrCodeString.split(";")
            return QRCodeInfoStellarInfoModel(
                    qrCodeType = if (info.isNotEmpty()) QRCodeType.values().firstOrNull {
                        info[0].toInt() == it.rawValue
                    } ?: QRCodeType.BY_EMAIL else QRCodeType.BY_STELLAR_ACCOUNT,
                    strAccountId = info[1]
            )
        }
    }


    fun toQRString(): String {
        return "%d;%s".format(qrCodeType.rawValue, strAccountId)
    }
}