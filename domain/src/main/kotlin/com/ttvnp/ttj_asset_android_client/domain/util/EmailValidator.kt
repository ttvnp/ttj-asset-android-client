package com.ttvnp.ttj_asset_android_client.domain.util

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
}
