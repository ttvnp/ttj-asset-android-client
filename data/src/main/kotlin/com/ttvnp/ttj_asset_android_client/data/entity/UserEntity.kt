package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import java.util.*

@Table
class UserEntity (
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

        @Column @Setter("isEmailVerified")
        var isEmailVerified: Boolean = false,

        @Column @Setter("isIdentified")
        var isIdentified: Boolean = false,

        @Column @Setter("updatedAt")
        var updatedAt: Date = Now()
)
