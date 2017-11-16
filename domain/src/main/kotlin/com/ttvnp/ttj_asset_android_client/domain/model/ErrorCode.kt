package com.ttvnp.ttj_asset_android_client.domain.model

enum class ErrorCode(val rawValue: Int) {
    NO_ERROR(0),
    ERROR_UNKNOWN_SERVER_ERROR(1),
    ERROR_DEVICE_NOT_REGISTERED(2),
    ERROR_CANNOT_REGISTER_DEVICE(3)
}
