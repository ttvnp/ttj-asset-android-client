package com.ttvnp.ttj_asset_android_client.data.service

import com.google.android.gms.safetynet.SafetyNetApi
import com.google.android.gms.tasks.Task
import com.ttvnp.ttj_asset_android_client.data.BuildConfig
import com.ttvnp.ttj_asset_android_client.data.driver.SafetyNetClient
import javax.inject.Inject

interface RecaptchaService {
    fun verifyWithRecaptcha(): Task<SafetyNetApi.RecaptchaTokenResponse>
}

class RecaptchaServiceImpl  @Inject constructor(val safetyNetClient: SafetyNetClient) : RecaptchaService {
    override fun verifyWithRecaptcha(): Task<SafetyNetApi.RecaptchaTokenResponse> {
        return safetyNetClient.verifyWithRecaptcha(BuildConfig.RECAPTCHA_API_SITE_KEY)
    }
}