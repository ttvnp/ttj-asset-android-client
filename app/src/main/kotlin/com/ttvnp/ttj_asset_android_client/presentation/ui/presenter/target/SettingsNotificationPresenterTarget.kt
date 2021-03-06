package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel

interface SettingsNotificationPresenterTarget : BasePresenterTarget {
    fun bindDeviceInfo(deviceModel: DeviceModel)
    fun bindUserInfo(userModel: UserModel)
}