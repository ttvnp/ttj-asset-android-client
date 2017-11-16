package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Single

interface DeviceRepository {

    fun getDevice(): Single<ModelWrapper<DeviceModel?>>

    fun register(): Single<ModelWrapper<DeviceModel?>>

    fun registerEmail(emailAddress: String): Single<DeviceModel>

    fun verifyEmail(verificationCode: String): Single<UserModel>
}
