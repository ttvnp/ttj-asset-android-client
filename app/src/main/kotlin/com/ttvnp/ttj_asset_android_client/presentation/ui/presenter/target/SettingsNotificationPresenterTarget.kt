package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel

interface SettingsNotificationPresenterTarget : BasePresenterTarget {
    fun bindDeviceInfo(deviceModel: DeviceModel)
}