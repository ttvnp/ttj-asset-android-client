package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.*
import io.reactivex.Single
import java.io.File

interface UserRepository {

    fun getStellarAccount(): Single<StellarAccountModel>

    fun getUser(forceRefresh: Boolean): Single<UserModel>

    fun checkValidationStellar(accountId: String, assetType: AssetType): Single<ErrorCode>

    fun updateUser(profileImageFile: File?,
                   firstName: String,
                   middleName: String,
                   lastName: String,
                   address: String,
                   genderType: Int,
                   dob: String,
                   cellphoneNumberNationalCode: String,
                   cellphoneNumber: String): Single<ModelWrapper<UserModel?>>

    fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>>

    fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?): Single<ModelWrapper<UserModel?>>

    fun changePassword(oldPassword: String, newPassword: String, retypePassword: String): Single<ModelWrapper<UserModel?>>

    fun updateNotificationSettings(grantEmailNotification: Boolean?): Single<ModelWrapper<UserModel?>>

    fun updateSecuritySettings(requirePasswordOnSend: Boolean?): Single<ModelWrapper<UserModel?>>

}