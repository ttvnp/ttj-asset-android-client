package com.ttvnp.ttj_asset_android_client.domain.model

class ErrorResponse(
        val statusCode: Int,
        val message: String,
        var error: Throwable
)