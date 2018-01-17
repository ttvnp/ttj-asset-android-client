package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import io.reactivex.Single
import java.io.File

interface UserRepository {
    fun getUser(forceRefresh: Boolean): Single<UserModel>
    fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String, genderType: Int, dob: String, cellphoneNumberNationalCode: String, cellphoneNumber: String): Single<ModelWrapper<UserModel?>>
    fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>>
    fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?): Single<ModelWrapper<UserModel?>>
}