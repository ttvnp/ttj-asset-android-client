package com.ttvnp.ttj_asset_android_client.domain.model

import android.content.Context
import com.ttvnp.ttj_asset_android_client.domain.R

class GenderType(val type: Int) {
    fun getGender(context: Context): String {
        return when (type) {
            Gender.MALE.rawValue -> context.resources.getString(R.string.male)
            Gender.FEMALE.rawValue -> context.resources.getString(R.string.female)
            else -> ""
        }
    }
}

enum class Gender(val rawValue: Int) {
    UNDEFINED(0),
    FEMALE(1),
    MALE(2)
}