package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Single
import java.io.File

interface UserRepository {
    fun getUser(): Single<UserModel>
    fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String): Single<ModelWrapper<UserModel?>>
    fun getTargetUser(emailAddress: String): Single<OtherUserModel>
}
