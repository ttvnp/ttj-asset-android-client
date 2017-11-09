package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json
import java.util.Date

class DeviceRegisterResponse(
        @Json(name = "accessToken") val accessToken: String = "",
        @Json(name = "accessTokenExpiry") val accessTokenExpiry: Date? = null
) : BaseResponse()

class DeviceRegisterEmailResponse(
) : BaseResponse()

class DeviceVerifyEmailResponse(
        @Json(name = "emailAddress") val emailAddress: String = "",
        @Json(name = "profileImageId") val profileImageId: Long = 0L,
        @Json(name = "profileImageURL") val profileImageURL: String = "",
        @Json(name = "firstName") val firstName: String = "",
        @Json(name = "middleName") val middleName: String = "",
        @Json(name = "lastName") val lastName: String = "",
        @Json(name = "address") val address: String = "",
        @Json(name = "isEmailVerified") val isEmailVerified: Boolean = false,
        @Json(name = "isIdentified") val isIdentified: Boolean = false
) : BaseResponse()
