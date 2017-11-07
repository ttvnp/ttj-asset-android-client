package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class UserEntity (

        @Column @Setter("emailAddress")
        var emailAddress: String = "",

        @Column @Setter("profileImageId")
        var profileImageId: Int = 0,

        @Column @Setter("firstName")
        var firstName: String = "",

        @Column @Setter("middleName")
        var middleName: String = "",

        @Column @Setter("lastName")
        var lastName: String = "",

        @Column @Setter("address")
        var address: String = "",

        @Column @Setter("isIdentified")
        var isIdentified: Boolean = false
)
