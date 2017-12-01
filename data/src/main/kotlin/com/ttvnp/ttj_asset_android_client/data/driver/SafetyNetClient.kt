package com.ttvnp.ttj_asset_android_client.data.driver

import android.content.Context
import com.google.android.gms.safetynet.SafetyNet
import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.tasks.Task
import javax.inject.Inject

class SafetyNetClient @Inject constructor(val context: Context) {
    private val client = SafetyNet.getClient(context)

    fun verifyWithRecaptcha(apiSiteKey: String): Task<SafetyNetApi.RecaptchaTokenResponse> {
        return client.verifyWithRecaptcha(apiSiteKey)
    }
}