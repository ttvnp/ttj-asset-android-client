package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Observable

interface DeviceRepository {

    fun register(): Observable<DeviceModel>

    fun registerEmail(emailAddress: String): Observable<DeviceModel>

    fun verifyEmail(verificationCode: String): Observable<UserModel>
}
