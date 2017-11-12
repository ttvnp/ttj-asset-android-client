package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json

class GetUserResponse(
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

class GetBalancesResponse(
        @Json(name = "balances") val balances: List<BalanceResponse>
) : BaseResponse()

class BalanceResponse(
        @Json(name = "assetType") val assetType: String = "",
        @Json(name = "amount") val amount: Long = 0L
)
