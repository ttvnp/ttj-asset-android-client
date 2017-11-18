package com.ttvnp.ttj_asset_android_client.domain.model

enum class ErrorCode(val rawValue: Int) {
    NO_ERROR(0),
    ERROR_ILLEGAL_DATA_STATE_ERROR(1),
    ERROR_UNKNOWN_SERVER_ERROR(2),
    ERROR_DEVICE_NOT_REGISTERED(3),
    ERROR_CANNOT_REGISTER_DEVICE(4),
    ERROR_CANNOT_UPDATE_USER(5),
    ERROR_CANNOT_UPDATE_DEVICE(6),
}
