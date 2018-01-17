package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class UserEntity(
        @Column @Setter("emailAddress")
        var emailAddress: String = "",

        @Column @Setter("profileImageId")
        var profileImageID: Long = 0,

        @Column @Setter("profileImageURL")
        var profileImageURL: String = "",

        @Column @Setter("firstName")
        var firstName: String = "",

        @Column @Setter("middleName")
        var middleName: String = "",

        @Column @Setter("lastName")
        var lastName: String = "",

        @Column @Setter("address")
        var address: String = "",

        @Column @Setter("genderType")
        var genderType: Int = 0,

        @Column @Setter("dateOfBirth")
        var dateOfBirth: String = "",

        @Column @Setter("cellphoneNumberNationalCode")
        var cellphoneNumberNationalCode: String = "",

        @Column @Setter("cellphoneNumber")
        var cellphoneNumber: String = "",

        @Column @Setter("idDocument1ImageURL")
        var idDocument1ImageURL: String = "",

        @Column @Setter("idDocument2ImageURL")
        var idDocument2ImageURL: String = "",

        @Column @Setter("isEmailVerified")
        var isEmailVerified: Boolean = false,

        @Column @Setter("isIdentified")
        var isIdentified: Boolean = false,

        @Column @Setter("identificationStatus")
        var identificationStatus: Int = 0,

        @Column @Setter("updatedAt")
        var updatedAt: Date = Now()
)
