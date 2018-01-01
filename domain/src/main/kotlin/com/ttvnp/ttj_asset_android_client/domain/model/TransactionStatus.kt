package com.ttvnp.ttj_asset_android_client.domain.model

enum class TransactionStatus(val rawValue: Int) {
    Unprocessed(0),
    Processed(1),
    Error(2),
}
