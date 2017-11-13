package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionsModel
import io.reactivex.Single

interface UserTransactionRepository {
    fun getTopByUserID(upperID: Long, limit: Long): Single<UserTransactionsModel>
}