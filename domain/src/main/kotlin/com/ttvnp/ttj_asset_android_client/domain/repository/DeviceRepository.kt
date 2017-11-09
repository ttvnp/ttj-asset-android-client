package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Single

interface DeviceRepository {

    fun register(): Single<DeviceModel>

    fun registerEmail(emailAddress: String): Single<DeviceModel>

    fun verifyEmail(verificationCode: String): Single<UserModel>
}
