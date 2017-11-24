package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.*
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import io.reactivex.Single
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
    @GET("users")
    fun getUser() : Call<GetUserResponse>

    @Headers("Accept: application/json")
    @Multipart
    @PATCH("users")
    fun updateUser(
            @Part profileImageFile: MultipartBody.Part,
            @Part("firstName") firstName: RequestBody,
            @Part("middleName") middleName: RequestBody,
            @Part("lastName") lastName: RequestBody,
            @Part("address") address: RequestBody
    ) : Single<UpdateUserResponse>

    @Headers("Accept: application/json")
    @GET("users/targets")
    fun getTargetUser(@Query("emailAddress") emailAddress: String) : Single<GetTargetUserResponse>

    @Headers("Accept: application/json")
    @GET("users/balances")
    fun getBalances() : Call<GetBalancesResponse>

    @Headers("Accept: application/json")
    @GET("users/transactions")
    fun getTransactions(@Query("upperUserTransactionID") upperUserTransactionID: Long) : Single<GetTransactionsResponse>

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("users/transactions")
    fun createTransaction(@Field("emailAddress") emailAddress: String, @Field("assetType") assetType: String, @Field("amount") amount: Long) : Single<CreateTransactionResponse>
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

    override fun getUser() : Call<GetUserResponse> {
        return service.getUser()
    }

    override fun updateUser(
            profileImageFile: MultipartBody.Part,
            firstName: RequestBody,
            middleName: RequestBody,
            lastName: RequestBody,
            address: RequestBody
    ): Single<UpdateUserResponse> {
        return service.updateUser(profileImageFile, firstName, middleName, lastName, address)
    }

    override fun getTargetUser(emailAddress: String) : Single<GetTargetUserResponse> {
        return service.getTargetUser(emailAddress)
    }

    override fun getBalances(): Call<GetBalancesResponse> {
        return service.getBalances()
    }

    override fun getTransactions(upperUserTransactionID: Long): Single<GetTransactionsResponse> {
        return service.getTransactions(upperUserTransactionID)
    }

    override fun createTransaction(emailAddress: String, assetType: String, amount: Long): Single<CreateTransactionResponse> {
        return service.createTransaction(emailAddress, assetType, amount)
    }
}
