package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json
import java.util.Date

class DeviceRegisterResponse(
        @Json(name = "accessToken") val accessToken: String = "",
        @Json(name = "accessTokenExpiry") val accessTokenExpiry: Date? = null
) : BaseResponse()
