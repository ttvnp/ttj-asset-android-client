package com.ttvnp.ttj_asset_android_client.domain.model

class UserModel(
        val emailAddress: String = "",
        val profileImageID: Long = 0L,
        val profileImageURL: String = "",
        val firstName: String = "",
        val middleName: String = "",
        val lastName: String = "",
        val address: String = "",
        val dateOfBirth: String = "",
        val genderType: GenderType = GenderType(0),
        val phoneNumber: PhoneNumber = PhoneNumber("", ""),
        val isDocument1ImageURL: String = "",
        val isDocument2ImageURL: String = "",
        val isEmailVerified: Boolean = false,
        val isIdentified: Boolean = false,
        val identificationStatus: IdentificationStatus = IdentificationStatus.Unchecked
) : BaseModel()
