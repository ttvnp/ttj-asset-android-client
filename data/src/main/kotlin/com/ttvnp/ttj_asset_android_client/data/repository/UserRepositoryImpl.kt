package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.OtherUserDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.OtherUserTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import com.ttvnp.ttj_asset_android_client.domain.util.addHour
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val userDataStore : UserDataStore,
        private val otherUserDataStore: OtherUserDataStore
) : UserRepository {

    override fun getUser(forceRefresh: Boolean): Single<UserModel> {
        return Single.create { subscriber ->
            var userModel: UserModel? = null
            var refresh = forceRefresh
            if (!refresh) {
                val userEntity = userDataStore.get()
                if (userEntity == null) {
                    refresh = true
                } else {
                    val localCacheExpiry = userEntity.updatedAt.addHour(24 * 7)
                    refresh = Now().after(localCacheExpiry)
                    if (!refresh) {
                        userModel = UserTranslator().translate(userEntity)
                    }
                }
            }
            if (refresh) {
                try {
                    userService.getUser().execute().body()?.let {
                        if (it.hasError()) {
                            return@let
                        }
                        var userEntity = UserEntity(
                                emailAddress = it.emailAddress,
                                profileImageID = it.profileImageID,
                                profileImageURL = it.profileImageURL,
                                firstName = it.firstName,
                                middleName =  it.middleName,
                                lastName = it.lastName,
                                address = it.address,
                                isEmailVerified = it.isEmailVerified,
                                isIdentified = it.isIdentified,
                                updatedAt = Now()
                        )
                        userEntity = userDataStore.update(userEntity)
                        userModel = UserTranslator().translate(userEntity)!!
                    }
                } catch (e: IOException) {
                    // ignore connection exception.
                    if (userModel == null) {
                        // get from local db
                        userModel = UserTranslator().translate(userDataStore.get())
                    }
                }
            }
            subscriber.onSuccess(userModel!!)
        }
    }

    override fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            val original = userDataStore.get()
            if (original == null) {
                subscriber.onSuccess(ModelWrapper<UserModel?>(null, ErrorCode.ERROR_ILLEGAL_DATA_STATE_ERROR))
                return@create
            }
            val profileImageFileBody: MultipartBody.Part
            if (profileImageFile == null) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOf())
                profileImageFileBody = MultipartBody.Part.createFormData("profileImageFile", "profile_image", requestFile)
            } else {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), profileImageFile)
                profileImageFileBody = MultipartBody.Part.createFormData("profileImageFile", "profile_image", requestFile)
            }
            val firstNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), firstName)
            val middleNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), middleName)
            val lastNameBody = RequestBody.create(MediaType.parse("multipart/form-data"), lastName)
            val addressBody = RequestBody.create(MediaType.parse("multipart/form-data"), address)
            try {
                userService.updateUser(profileImageFileBody, firstNameBody, middleNameBody, lastNameBody, addressBody).execute().body()!!.let { response ->
                    if (response.hasError()) {
                        // TODO handle server error codes.
                        subscriber.onSuccess(ModelWrapper<UserModel?>(UserTranslator().translate(original), ErrorCode.ERROR_CANNOT_UPDATE_USER))
                        return@create
                    }
                    var ue = UserEntity(
                            emailAddress = original.emailAddress,
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName,
                            address = response.address,
                            isEmailVerified = response.isEmailVerified,
                            isIdentified = response.isIdentified,
                            updatedAt = Now()
                    )
                    ue = userDataStore.update(ue)
                    subscriber.onSuccess(ModelWrapper<UserModel?>(UserTranslator().translate(ue), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<UserModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
            }
        }
    }

    override fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>> {
        val getModelFromLocal: () -> OtherUserModel? = {
            val otherUserEntity = otherUserDataStore.getByEmailAddress(emailAddress)
            OtherUserTranslator().translate(otherUserEntity)
        }
        return Single.create { subscriber ->
            var model = getModelFromLocal()
            if (model == null) {
                try {
                    userService.getTargetUser(emailAddress).execute().body()?.let { response ->
                        if (response.hasError()) return@let
                        var otherUserEntity = OtherUserEntity(
                                id = response.id,
                                emailAddress = response.emailAddress,
                                profileImageID = response.profileImageID,
                                profileImageURL = response.profileImageURL,
                                firstName = response.firstName,
                                middleName =  response.middleName,
                                lastName = response.lastName
                        )
                        otherUserEntity = otherUserDataStore.update(otherUserEntity)
                        model = OtherUserTranslator().translate(otherUserEntity)!!
                    }
                } catch (e: IOException) {
                    // ignore exception.
                }
            }
            if (model == null) {
                subscriber.onSuccess(ModelWrapper<OtherUserModel?>(null, ErrorCode.ERROR_CANNOT_FIND_TARGET_USER))
            } else {
                subscriber.onSuccess(ModelWrapper<OtherUserModel?>(model, ErrorCode.NO_ERROR))
            }
        }
    }
}
