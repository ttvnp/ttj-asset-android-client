package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.OtherUserDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.OtherUserTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import io.reactivex.Single
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