package com.ttvnp.ttj_asset_android_client.data.entity

import io.realm.RealmObject

open class UserEntity (
    open var emailAddress: String = "",
    open var profileImageId: Int = 0,
    open var firstName: String = "",
    open var middleName: String = "",
    open var lastName: String = "",
    open var address: String = "",
    open var isIdentified: Boolean = false
) : RealmObject()