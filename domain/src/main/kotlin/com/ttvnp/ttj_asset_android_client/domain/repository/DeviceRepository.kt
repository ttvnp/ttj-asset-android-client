package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import io.reactivex.Observable

interface DeviceRepository {

    fun register() : Observable<DeviceModel>

    fun get() : Observable<DeviceModel>

    fun save(model: DeviceModel): Observable<DeviceModel>
}
