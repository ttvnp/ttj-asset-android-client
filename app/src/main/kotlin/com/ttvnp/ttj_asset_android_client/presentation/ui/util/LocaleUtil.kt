package com.ttvnp.ttj_asset_android_client.presentation.ui.util

import android.content.res.Resources
import android.os.Build
import java.util.*

fun changeLocale(resources: Resources, locale: Locale) {
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}

fun getCurrentLocale(resources: Resources): Locale {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return resources.configuration.locales[0]
    }

    return resources.configuration.locale
}