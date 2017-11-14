package com.ttvnp.ttj_asset_android_client.data.service

import com.squareup.moshi.Moshi
import com.ttvnp.ttj_asset_android_client.data.service.adapter.DateAdapter
import com.ttvnp.ttj_asset_android_client.data.service.response.GetBalancesResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.GetTargetUserResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.GetTransactionsResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.GetUserResponse
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface UserService {
    @Headers("Accept: application/json")
    @GET("users")
    fun getUser() : Single<GetUserResponse>

    @Headers("Accept: application/json")
    @GET("users/targets")
    fun getTargetUser(@Query("emailAddress") emailAddress: String) : Single<GetTargetUserResponse>

    @Headers("Accept: application/json")
    @GET("users/balances")
    fun getBalances() : Single<GetBalancesResponse>

    @Headers("Accept: application/json")
    @GET("users/transactions")
    fun getTransactions(@Query("upperUserTransactionID") upperUserTransactionID: Long) : Single<GetTransactionsResponse>
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

    override fun getUser() : Single<GetUserResponse> {
        return service.getUser()
    }

    override fun getTargetUser(emailAddress: String) : Single<GetTargetUserResponse> {
        return service.getTargetUser(emailAddress)
    }

    override fun getBalances() : Single<GetBalancesResponse> {
        return service.getBalances()
    }

    override fun getTransactions(upperUserTransactionID: Long): Single<GetTransactionsResponse> {
        return service.getTransactions(upperUserTransactionID)
    }
}
