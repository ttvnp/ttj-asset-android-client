package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class UserEntity(
        @Column(defaultExpr = "") @Setter("emailAddress")
        var emailAddress: String = "",

        @Column(defaultExpr = "0") @Setter("profileImageId")
        var profileImageID: Long = 0,

        @Column(defaultExpr = "") @Setter("profileImageURL")
        var profileImageURL: String = "",

        @Column(defaultExpr = "") @Setter("firstName")
        var firstName: String = "",

        @Column(defaultExpr = "") @Setter("middleName")
        var middleName: String = "",

        @Column(defaultExpr = "") @Setter("lastName")
        var lastName: String = "",

        @Column(defaultExpr = "") @Setter("address")
        var address: String = "",

        @Column(defaultExpr = "0") @Setter("genderType")
        var genderType: Int = 0,

        @Column(defaultExpr = "") @Setter("dateOfBirth")
        var dateOfBirth: String = "",

        @Column(defaultExpr = "") @Setter("cellphoneNumberNationalCode")
        var cellphoneNumberNationalCode: String = "",

        @Column(defaultExpr = "") @Setter("cellphoneNumber")
        var cellphoneNumber: String = "",

        @Column(defaultExpr = "") @Setter("idDocument1ImageURL")
        var idDocument1ImageURL: String = "",

        @Column(defaultExpr = "") @Setter("idDocument2ImageURL")
        var idDocument2ImageURL: String = "",

        @Column(defaultExpr = "false") @Setter("isEmailVerified")
        var isEmailVerified: Boolean = false,

        @Column(defaultExpr = "false") @Setter("isIdentified")
        var isIdentified: Boolean = false,

        @Column(defaultExpr = "0") @Setter("identificationStatus")
        var identificationStatus: Int = 0,

        @Column(defaultExpr = "false") @Setter("grantEmailNotification")
        var grantEmailNotification: Boolean = false,

        @Column(defaultExpr = "false") @Setter("requirePasswordOnSend")
        var requirePasswordOnSend: Boolean = false,

        @Column(defaultExpr = "now") @Setter("updatedAt")
        var updatedAt: Date = Now()

)
