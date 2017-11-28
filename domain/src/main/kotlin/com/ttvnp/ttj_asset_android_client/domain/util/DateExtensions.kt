package com.ttvnp.ttj_asset_android_client.domain.util

import java.util.*

fun Date.addHour(hours: Int): Date {
    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    cal.time = this
    cal.add(Calendar.HOUR, hours)
    return cal.time
}