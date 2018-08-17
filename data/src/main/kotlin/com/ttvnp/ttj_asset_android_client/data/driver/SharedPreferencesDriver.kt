package com.ttvnp.ttj_asset_android_client.data.driver

import android.content.SharedPreferences
import javax.inject.Inject

interface
SharedPreferencesDriver {
    fun getString(key: String, defaultValue: String = ""): String?
    fun putString(key: String, value: String)
    fun remove()
}

class SharedPreferencesDriverImpl @Inject constructor(
        val sharedPreferences: SharedPreferences
) : SharedPreferencesDriver {
    override fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun remove() {
        sharedPreferences.edit().clear().apply()
    }
}
