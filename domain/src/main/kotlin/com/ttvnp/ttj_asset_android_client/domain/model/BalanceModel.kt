package com.ttvnp.ttj_asset_android_client.domain.model

import java.text.NumberFormat

class BalanceModel(
        val assetType : String = "",
        val amount : Long = 0L
) {
    companion object {
        val ASSET_TYPE_POINT = "SNP"
        val ASSET_TYPE_COIN = "SNC"
    }
    fun getAmountFormatString(): String {
        val nf = NumberFormat.getNumberInstance()
        return nf.format(amount)
    }
}
