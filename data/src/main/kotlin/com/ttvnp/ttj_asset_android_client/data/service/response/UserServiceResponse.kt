package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json
import java.util.*

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

class GetTransactionsResponse(
        @Json(name = "hasMore") val hasMore: Boolean = false,
        @Json(name = "userTransactions") val userTransactions: List<TransactionResponse>
) : BaseResponse()

class TransactionResponse(
        @Json(name = "id") val id: Long = 0L,
        @Json(name = "loggedAt") val loggedAt: Date? = null,
        @Json(name = "transactionType") val transactionType: Int = 0,
        @Json(name = "targetUserID") val targetUserID: Long = 0L,
        @Json(name = "targetUserEmailAddress") val targetUserEmailAddress: String = "",
        @Json(name = "targetUserProfileImageID") val targetUserProfileImageID: Long = 0L,
        @Json(name = "targetUserProfileImageURL") val targetUserProfileImageURL: String = "",
        @Json(name = "targetUserFirstName") val targetUserFirstName: String = "",
        @Json(name = "targetUserMiddleName") val targetUserMiddleName: String = "",
        @Json(name = "targetUserLastName") val targetUserLastName: String = "",
        @Json(name = "assetType") val assetType: String = "",
        @Json(name = "amount") val amount: Long = 0L
)
