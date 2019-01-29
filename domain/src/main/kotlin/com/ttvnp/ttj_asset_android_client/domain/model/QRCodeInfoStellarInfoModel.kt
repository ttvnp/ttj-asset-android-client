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
                        info[0] == it.rawValue
                    } ?: QRCodeType.BY_EMAIL else QRCodeType.BY_STELLAR_ACCOUNT,
                    strAccountId = if (info.size < 2) "" else info[1]
            )
        }
    }


    fun toQRString(): String {
        return "%s;%s".format(qrCodeType.rawValue, strAccountId)
    }
}