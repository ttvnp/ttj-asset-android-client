package com.ttvnp.ttj_asset_android_client.presentation.ui.util

import android.content.res.Resources
import java.util.*

fun changeLocale(resources: Resources, locale: Locale) {
    Locale.setDefault(locale)
    val config = resources.configuration
    config.setLocale(locale)
    resources.updateConfiguration(config, resources.displayMetrics)
}