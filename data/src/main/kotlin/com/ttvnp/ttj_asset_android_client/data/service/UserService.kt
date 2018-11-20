package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.*
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.util.ServerCryptoUtil
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface UserService {
    @Headers("Accept: application/json")
    @GET("users/str_receive_account")
    fun getStellarAccount(): Call<GetStellarAccountResponse>

    @Headers("Accept: application/json")
    @GET("users")
    fun getUser(): Call<GetUserResponse>

    @Headers("Accept: application/json")
    @Multipart
    @PATCH("users")
    fun updateUser(
            @Part profileImageFile: MultipartBody.Part,
            @Part("firstName") firstName: RequestBody,
            @Part("middleName") middleName: RequestBody,
            @Part("lastName") lastName: RequestBody,
            @Part("address") address: RequestBody,
            @Part("genderType") genderType: RequestBody,
            @Part("dateOfBirth") dateOfBirth: RequestBody,
            @Part("cellphoneNumberNationalCode") cellphoneNumberNationalCode: RequestBody,
            @Part("cellphoneNumber") cellphoneNumber: RequestBody
    ): Call<UpdateUserResponse>

    @Headers("Accept: application/json")
    @Multipart
    @POST("users/id_document")
    fun uploadIdDocument(
            @Part faceImageFile: MultipartBody.Part,
            @Part addressImageFile: MultipartBody.Part
    ): Call<UpdateUserResponse>

    @Headers("Accept: application/json")
    @GET("users/targets")
    fun getTargetUser(@Query("emailAddress") emailAddress: String): Call<GetTargetUserResponse>

    @Headers("Accept: application/json")
    @GET("users/balances")
    fun getBalances(): Call<GetBalancesResponse>

    @Headers("Accept: application/json")
    @GET("users/transactions")
    fun getTransactions(@Query("upperUserTransactionID") upperUserTransactionID: Long): Call<GetTransactionsResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/transactions")
    fun createTransaction(@Header("credential") credential: String,
                          @Field("emailAddress") emailAddress: String,
                          @Field("assetType") assetType: String,
                          @Field("amount") amount: Long,
                          @Field("password") password: String
    ): Call<CreateTransactionResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/transactions_external")
    fun createExternalTransaction(
            @Header("credential") credential: String,
            @Field("strAccountID") strAccountID: String,
            @Field("strMemoText") strMemoText: String,
            @Field("assetType") assetType: String,
            @Field("amount") amount: Long,
            @Field("password") password: String
    ): Call<CreateTransactionResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PATCH("users/password_on_import")
    fun changePassword(@Field("current_password") oldPassword: String,
                       @Field("new_password") newPassword: String,
                       @Field("new_password2")retypePassword: String): Call<UserResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PATCH("users/notification_settings")
    fun updateNotificationSettings(
            @Field("grantEmailNotification") grantEmailNotification: Boolean
    ): Call<UpdateUserResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @PATCH("users/security_settings")
    fun updateSecuritySettings(
            @Field("requirePasswordOnSend") requirePasswordOnSend: Boolean
    ): Call<UpdateUserResponse>

}

class UserServiceImpl(
        deviceInfoDataStore: DeviceInfoDataStore,
        deviceDataStore: DeviceDataStore,
        deviceServiceWithNoAuth: DeviceServiceWithNoAuth
) : BaseAuthService(deviceInfoDataStore, deviceDataStore, deviceServiceWithNoAuth), UserService {

    private val service: UserService

    init {
        val moshi = Moshi.Builder()
                .add(DateAdapter.FACTORY)
                .build()
        val okClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .addInterceptor(getRequestInterceptor())
                .addInterceptor(getAccessTokenInterceptor())
                .build()
        val builder = Retrofit.Builder()
                .client(okClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .baseUrl(getBaseURL())
                .build()
        service = builder.create(UserService::class.java)
    }

    override fun getStellarAccount(): Call<GetStellarAccountResponse> {
        return service.getStellarAccount()
    }

    override fun getUser(): Call<GetUserResponse> {
        return service.getUser()
    }

    override fun updateUser(
            profileImageFile: MultipartBody.Part,
            firstName: RequestBody,
            middleName: RequestBody,
            lastName: RequestBody,
            address: RequestBody,
            genderType: RequestBody,
            dateOfBirth: RequestBody,
            cellphoneNumberNationalCode: RequestBody,
            cellphoneNumber: RequestBody
    ): Call<UpdateUserResponse> {
        return service.updateUser(profileImageFile, firstName, middleName, lastName, address, genderType, dateOfBirth, cellphoneNumberNationalCode, cellphoneNumber)
    }

    override fun uploadIdDocument(faceImageFile: MultipartBody.Part, addressImageFile: MultipartBody.Part): Call<UpdateUserResponse> {
        return service.uploadIdDocument(faceImageFile, addressImageFile)
    }

    override fun getTargetUser(emailAddress: String): Call<GetTargetUserResponse> {
        return service.getTargetUser(emailAddress)
    }

    override fun getBalances(): Call<GetBalancesResponse> {
        return service.getBalances()
    }

    override fun getTransactions(upperUserTransactionID: Long): Call<GetTransactionsResponse> {
        return service.getTransactions(upperUserTransactionID)
    }

    override fun createTransaction(credential: String,
                                   emailAddress: String,
                                   assetType: String,
                                   amount: Long,
                                   password: String
    ): Call<CreateTransactionResponse> {
        return service.createTransaction(ServerCryptoUtil.encrypt(credential), emailAddress, assetType, amount, password)
    }

    override fun createExternalTransaction(credential: String, strAccountID: String, strMemoText: String, assetType: String, amount: Long, password: String): Call<CreateTransactionResponse> {
        return service.createExternalTransaction(ServerCryptoUtil.encrypt(credential), strAccountID, strMemoText, assetType, amount, password)
    }

    override fun changePassword(oldPassword: String, newPassword: String, retypePassword: String): Call<UserResponse> {
        return service.changePassword(oldPassword, newPassword, retypePassword)
    }

    override fun updateNotificationSettings(grantEmailNotification: Boolean): Call<UpdateUserResponse> {
        return service.updateNotificationSettings(grantEmailNotification)
    }

    override fun updateSecuritySettings(requirePasswordOnSend: Boolean): Call<UpdateUserResponse> {
        return service.updateSecuritySettings(requirePasswordOnSend)
    }
}
