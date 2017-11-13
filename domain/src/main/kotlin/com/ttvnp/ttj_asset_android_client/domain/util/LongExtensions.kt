package com.ttvnp.ttj_asset_android_client.domain.util

import java.text.NumberFormat

fun Long.formatString(): String {
    val nf = NumberFormat.getNumberInstance()
    return nf.format(this)
}
