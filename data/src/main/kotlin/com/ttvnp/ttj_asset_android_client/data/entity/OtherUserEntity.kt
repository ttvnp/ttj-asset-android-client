package com.ttvnp.ttj_asset_android_client.data.entity

import com.github.gfx.android.orma.annotation.Column
import com.github.gfx.android.orma.annotation.PrimaryKey
import com.github.gfx.android.orma.annotation.Setter
import com.github.gfx.android.orma.annotation.Table

@Table
class OtherUserEntity (
        @PrimaryKey(auto = false, autoincrement = false)
        @Setter("id")
        val id: Long = 0L,

        @Column(indexed = true)
        @Setter("emailAddress")
        var emailAddress: String = "",

        @Column
        @Setter("profileImageId")
        var profileImageID: Long = 0,

        @Column
        @Setter("profileImageURL")
        var profileImageURL: String = "",

        @Column
        @Setter("firstName")
        var firstName: String = "",

        @Column
        @Setter("middleName")
        var middleName: String = "",

        @Column
        @Setter("lastName")
        var lastName: String = ""
)
