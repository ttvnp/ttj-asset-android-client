package com.ttvnp.ttj_asset_android_client.data.crypto

enum class BlockMode(val rawValue: String) {

    ECB("ECB"),
    CBC("CBC"),
    CTR("CTR"),
    GCM("GCM"),
}