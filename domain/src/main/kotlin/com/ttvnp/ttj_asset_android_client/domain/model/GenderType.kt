package com.ttvnp.ttj_asset_android_client.domain.model

import android.content.Context
import com.ttvnp.ttj_asset_android_client.domain.R

class GenderType(val type: Int) {
    fun getGender(context: Context?): String {
        context?.let {
            return when (type) {
                Gender.MALE.rawValue -> it.resources.getString(R.string.male)
                Gender.FEMALE.rawValue -> it.resources.getString(R.string.female)
                else -> ""
            }
        } ?: return ""
    }
}

enum class Gender(val rawValue: Int) {
    UNDEFINED(0),
    FEMALE(1),
    MALE(2)
}