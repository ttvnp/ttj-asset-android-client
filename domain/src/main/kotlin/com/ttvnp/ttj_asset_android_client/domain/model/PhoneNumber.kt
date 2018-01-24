package com.ttvnp.ttj_asset_android_client.domain.model

class PhoneNumber(val nationalCode: String, val cellphoneNumber: String) {
    fun getCellphoneNumberWithNationalCode(): String {
        if (nationalCode.isNotBlank() && cellphoneNumber.isNotBlank()) {
            return String.format("+%s %s", nationalCode, cellphoneNumber)
        }
        return ""
    }
}