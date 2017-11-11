package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Single

interface UserRepository {
    fun getUser(): Single<UserModel>
}