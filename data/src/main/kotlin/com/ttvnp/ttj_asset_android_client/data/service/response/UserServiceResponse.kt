package com.ttvnp.ttj_asset_android_client.data.service.response

import com.squareup.moshi.Json
import java.util.*

class GetStellarAccountResponse(
        @Json(name = "strAccountID") val strAccountID: String = "",
        @Json(name = "strDepositMemoText") val strDepositMemoText: String = "",
        @Json(name = "message") val message: String = ""
) : BaseResponse()

class GetUserResponse(
        @Json(name = "emailAddress") val emailAddress: String = "",
        @Json(name = "profileImageID") val profileImageID: Long = 0L,
        @Json(name = "profileImageURL") val profileImageURL: String = "",
        @Json(name = "firstName") val firstName: String = "",
        @Json(name = "middleName") val middleName: String = "",
        @Json(name = "lastName") val lastName: String = "",
        @Json(name = "address") val address: String = "",
        @Json(name = "genderType") val genderType: Int = 0,
        @Json(name = "dateOfBirth") val dateOfBirth: String = "",
        @Json(name = "cellphoneNumberNationalCode") val cellphoneNumberNationalCode: String = "",
        @Json(name = "cellphoneNumber") val cellphoneNumber: String = "",
        @Json(name = "idDocument1ImageURL") val idDocument1ImageURL: String = "",
        @Json(name = "idDocument2ImageURL") val idDocument2ImageURL: String = "",
        @Json(name = "isEmailVerified") val isEmailVerified: Boolean = false,
        @Json(name = "isIdentified") val isIdentified: Boolean = false,
        @Json(name = "identificationStatus") val identificationStatus: Int = 0,
        @Json(name = "grantEmailNotification") val grantEmailNotification: Boolean = false,
        @Json(name = "requirePasswordOnSend") val requirePasswordOnSend: Boolean = false
) : BaseResponse()

class UpdateUserResponse(
        @Json(name = "emailAddress") val emailAddress: String = "",
        @Json(name = "profileImageID") val profileImageID: Long = 0L,
        @Json(name = "profileImageURL") val profileImageURL: String = "",
        @Json(name = "firstName") val firstName: String = "",
        @Json(name = "middleName") val middleName: String = "",
        @Json(name = "lastName") val lastName: String = "",
        @Json(name = "address") val address: String = "",
        @Json(name = "genderType") val genderType: Int = 0,
        @Json(name = "dateOfBirth") val dateOfBirth: String = "",
        @Json(name = "cellphoneNumberNationalCode") val cellphoneNumberNationalCode: String = "",
        @Json(name = "cellphoneNumber") val cellphoneNumber: String = "",
        @Json(name = "idDocument1ImageURL") val idDocument1ImageURL: String = "",
        @Json(name = "idDocument2ImageURL") val idDocument2ImageURL: String = "",
        @Json(name = "isEmailVerified") val isEmailVerified: Boolean = false,
        @Json(name = "isIdentified") val isIdentified: Boolean = false,
        @Json(name = "identificationStatus ") val identificationStatus: Int = 0,
        @Json(name = "grantEmailNotification") val grantEmailNotification: Boolean = false,
        @Json(name = "requirePasswordOnSend") val requirePasswordOnSend: Boolean = false
) : BaseResponse()

class GetTargetUserResponse(
        @Json(name = "id") val id: Long = 0L,
        @Json(name = "emailAddress") val emailAddress: String = "",
        @Json(name = "profileImageID") val profileImageID: Long = 0L,
        @Json(name = "profileImageURL") val profileImageURL: String = "",
        @Json(name = "firstName") val firstName: String = "",
        @Json(name = "middleName") val middleName: String = "",
        @Json(name = "lastName") val lastName: String = ""
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
        @Json(name = "transactionStatus") val transactionStatus: Int = 0,
        @Json(name = "transactionType") val transactionType: Int = 0,
        @Json(name = "targetUserID") val targetUserID: Long = 0L,
        @Json(name = "targetUserEmailAddress") val targetUserEmailAddress: String = "",
        @Json(name = "targetUserProfileImageID") val targetUserProfileImageID: Long = 0L,
        @Json(name = "targetUserProfileImageURL") val targetUserProfileImageURL: String = "",
        @Json(name = "targetUserFirstName") val targetUserFirstName: String = "",
        @Json(name = "targetUserMiddleName") val targetUserMiddleName: String = "",
        @Json(name = "targetUserLastName") val targetUserLastName: String = "",
        @Json(name = "targetStrAccountID") val targetStrAccountID: String = "",
        @Json(name = "strMemoText") val strMemoText: String = "",
        @Json(name = "assetType") val assetType: String = "",
        @Json(name = "amount") val amount: Long = 0L
)

class CreateTransactionResponse(
        @Json(name = "userTransaction") val userTransaction: TransactionResponse,
        @Json(name = "balances") val balances: List<BalanceResponse>
) : BaseResponse()

