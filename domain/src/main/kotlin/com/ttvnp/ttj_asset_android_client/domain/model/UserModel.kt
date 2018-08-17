package com.ttvnp.ttj_asset_android_client.domain.model

import android.content.Context

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
        val identificationStatus: IdentificationStatus = IdentificationStatus.Unchecked,
        val grantEmailNotification: Boolean = false,
        val requirePasswordOnSend: Boolean = false
) : BaseModel() {
    fun hasAllNecessaryInfo(context: Context): Boolean {
        return (
                this.firstName.isNotEmpty()
                && this.lastName.isNotEmpty()
                && this.address.isNotBlank()
                && this.dateOfBirth.isNotEmpty()
                && this.genderType.getGender(context).isNotEmpty()
                && this.phoneNumber.getCellphoneNumberWithNationalCode().isNotEmpty()
                )
    }
}
