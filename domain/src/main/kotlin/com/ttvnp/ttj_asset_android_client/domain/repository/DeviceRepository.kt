package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel

interface DeviceRepository {

    fun register() : DeviceModel?

    fun get() : DeviceModel?

    fun save(model: DeviceModel): DeviceModel
}
