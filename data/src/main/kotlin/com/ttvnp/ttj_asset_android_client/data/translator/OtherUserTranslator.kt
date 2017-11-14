package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import com.ttvnp.ttj_asset_android_client.domain.model.OtherUserModel

internal class OtherUserTranslator : BaseTranslator<OtherUserModel, OtherUserEntity>() {

    override internal fun translate(entity: OtherUserEntity?): OtherUserModel? {
        if (entity == null) {
            return null
        }
        return OtherUserModel(
                id = entity.id,
                emailAddress = entity.emailAddress,
                profileImageID = entity.profileImageID,
                profileImageURL = entity.profileImageURL,
                firstName = entity.firstName,
                middleName = entity.middleName,
                lastName = entity.lastName
        )
    }
}