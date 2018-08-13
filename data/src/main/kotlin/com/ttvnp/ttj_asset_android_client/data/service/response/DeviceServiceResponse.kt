package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json
import java.util.*

class DeviceResponse(
        @Json(name = "accessToken") val accessToken: String = "",
        @Json(name = "accessTokenExpiry") val accessTokenExpiry: Date? = null,
        @Json(name = "isActivated") val isActivated: Boolean = false,
        @Json(name = "deviceToken") val deviceToken: String = "",
        @Json(name = "grantPushNotification") val grantPushNotification: Boolean = false,
        @Json(name = "grantEmailNotification") val grantEmailNotification: Boolean = false
) : BaseResponse()

class DeviceRegisterEmailResponse(
        @Json(name = "isEmailInUse") val isEmailInUse: Boolean = false
) : BaseResponse()

class DeviceVerifyEmailResponse(
        @Json(name = "device") val device: DeviceResponse2,
        @Json(name = "user") val user: UserResponse
) : BaseResponse()

class DeviceResponse2(
        @Json(name = "accessToken") val accessToken: String = "",
        @Json(name = "accessTokenExpiry") val accessTokenExpiry: Date? = null,
        @Json(name = "isActivated") val isActivated: Boolean = false,
        @Json(name = "deviceToken") val deviceToken: String = "",
        @Json(name = "grantPushNotification") val grantPushNotification: Boolean = false,
        @Json(name = "grantEmailNotification") val grantEmailNotification: Boolean = false
)

class UserResponse(
        @Json(name = "emailAddress") val emailAddress: String = "",
        @Json(name = "profileImageID") val profileImageID: Long = 0L,
        @Json(name = "profileImageURL") val profileImageURL: String = "",
        @Json(name = "firstName") val firstName: String = "",
        @Json(name = "middleName") val middleName: String = "",
        @Json(name = "lastName") val lastName: String = "",
        @Json(name = "address") val address: String = "",
        @Json(name = "isEmailVerified") val isEmailVerified: Boolean = false,
        @Json(name = "isIdentified") val isIdentified: Boolean = false
) : BaseResponse()