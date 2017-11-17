package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.UpdateUserResponse
import com.ttvnp.ttj_asset_android_client.data.store.OtherUserDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.DeviceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.OtherUserTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val userDataStore : UserDataStore,
        private val otherUserDataStore: OtherUserDataStore
) : UserRepository {

    override fun getUser(): Single<UserModel> {
        return userService.getUser().map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
            var userEntity = UserEntity(
                    emailAddress = response.emailAddress,
                    profileImageID = response.profileImageID,
                    profileImageURL = response.profileImageURL,
                    firstName = response.firstName,
                    middleName =  response.middleName,
                    lastName = response.lastName,
                    address = response.address,
                    isEmailVerified = response.isEmailVerified,
                    isIdentified = response.isIdentified
            )
            userEntity = userDataStore.update(userEntity)
            UserTranslator().translate(userEntity)!!
        }.onErrorReturn { e ->
            val userEntity = userDataStore.get()
            if (userEntity == null) {
                throw e
            }
            UserTranslator().translate(userEntity)!!
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
            userService.updateUser(profileImageFileBody, firstNameBody, middleNameBody, lastNameBody, addressBody).subscribeWith(object : DisposableSingleObserver<UpdateUserResponse>() {
                override fun onSuccess(response: UpdateUserResponse) {
                    val serviceResult: ModelWrapper<UserModel?>
                    if (response.hasError()) {
                        serviceResult = ModelWrapper<UserModel?>(UserTranslator().translate(original), ErrorCode.ERROR_CANNOT_UPDATE_USER)
                    } else {
                        var ue = UserEntity(
                                emailAddress = original.emailAddress,
                                profileImageID = response.profileImageID,
                                profileImageURL = response.profileImageURL,
                                firstName = response.firstName,
                                middleName = response.middleName,
                                lastName = response.lastName,
                                address = response.address,
                                isEmailVerified = response.isEmailVerified,
                                isIdentified = response.isIdentified
                        )
                        ue = userDataStore.update(ue)
                        serviceResult = ModelWrapper<UserModel?>(UserTranslator().translate(ue), ErrorCode.NO_ERROR)
                    }
                    subscriber.onSuccess(serviceResult)
                }
                override fun onError(e: Throwable) {
                    subscriber.onSuccess(ModelWrapper<UserModel?>(UserTranslator().translate(original), ErrorCode.ERROR_UNKNOWN_SERVER_ERROR))
                }
            })
        }
    }

    override fun getTargetUser(emailAddress: String): Single<OtherUserModel> {
        return userService.getTargetUser(emailAddress).map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
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
            OtherUserTranslator().translate(otherUserEntity)!!
        }.onErrorReturn { e ->
            val otherUserEntity = otherUserDataStore.getByEmailAddress(emailAddress)
            if (otherUserEntity == null) {
                throw e
            }
            OtherUserTranslator().translate(otherUserEntity)!!
        }
    }
}
