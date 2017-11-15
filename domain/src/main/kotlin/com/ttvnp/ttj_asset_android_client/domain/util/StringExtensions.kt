package com.ttvnp.ttj_asset_android_client.domain.util

fun String.prependIfNotBlank(s: String): String {
    return if (this.isBlank()) this else s + this
}

fun String.isValidAmount(): Boolean {
    val amount = this.toLongOrNull()
    return amount != null && 0 < amount
}

fun String.toAmount(): Long {
    return this.toLong()
}