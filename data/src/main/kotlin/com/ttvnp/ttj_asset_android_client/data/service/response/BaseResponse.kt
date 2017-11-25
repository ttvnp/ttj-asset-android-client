package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json

open class BaseResponse(
        @Json(name = "exitCode") val exitCode: Int = 0,
        @Json(name = "errorCode") val errorCode: Int = 0
) {
    open fun hasError(): Boolean {
        return exitCode != 0
    }
}
