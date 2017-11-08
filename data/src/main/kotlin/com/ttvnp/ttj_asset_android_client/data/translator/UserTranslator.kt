package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel

internal class UserTranslator : BaseTranslator<UserModel, UserEntity>() {

    override internal fun translate(entity: UserEntity?): UserModel? {
        if (entity == null) {
            return null
        }
        return UserModel(
                emailAddress = entity.emailAddress,
                profileImageID = entity.profileImageID,
                profileImageURL = entity.profileImageURL,
                firstName = entity.firstName,
                middleName = entity.middleName,
                lastName = entity.lastName,
                address = entity.address,
                isEmailVerified = entity.isEmailVerified,
                isIdentified = entity.isIdentified
        )
    }
}