package com.ttvnp.ttj_asset_android_client.domain.model

class OtherUserModel(
        val id: Long = 0L,
        val emailAddress : String = "",
        val profileImageID : Long = 0L,
        val profileImageURL : String = "",
        val firstName : String = "",
        val middleName : String = "",
        val lastName : String = ""
) : BaseModel()