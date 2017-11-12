package com.ttvnp.ttj_asset_android_client.domain.util

fun String.prependIfNotBlank(s: String): String {
    return if (this.isBlank()) this else s + this
}