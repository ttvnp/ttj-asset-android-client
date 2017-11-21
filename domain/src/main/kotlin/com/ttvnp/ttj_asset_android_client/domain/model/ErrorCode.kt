package com.ttvnp.ttj_asset_android_client.domain.model

enum class ErrorCode(val rawValue: Int) {
    NO_ERROR(0),

    // system error
    ERROR_CANNOT_CONNECT_TO_SERVER(1),

    // service error
    ERROR_ILLEGAL_DATA_STATE_ERROR(2),
    ERROR_UNKNOWN_SERVER_ERROR(3),
    ERROR_DEVICE_NOT_REGISTERED(4),
    ERROR_CANNOT_REGISTER_DEVICE(5),
    ERROR_CANNOT_UPDATE_USER(6),
    ERROR_CANNOT_UPDATE_DEVICE(7),
}
