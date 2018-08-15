package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.util.Now

internal class DeviceTranslator: BaseTranslator<DeviceModel, DeviceEntity>() {

    override fun translate(entity: DeviceEntity?): DeviceModel? {
        if (entity == null) {
            return null
        }
        return DeviceModel(
                accessToken = entity.accessToken,
                accessTokenExpiry = entity.accessTokenExpiry?: Now(),
                isActivated = entity.isActivated,
                deviceToken = entity.deviceToken,
                grantPushNotification = entity.grantPushNotification
        )
    }
}
