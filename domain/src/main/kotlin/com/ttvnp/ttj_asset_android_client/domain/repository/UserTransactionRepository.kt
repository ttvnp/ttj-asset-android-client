package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.*
import io.reactivex.Single

interface UserTransactionRepository {
    fun getTopByUserID(upperID: Long, limit: Long, forceRefresh: Boolean): Single<UserTransactionsModel>
    fun createTransaction(sendInfoModel: SendInfoModel, onReceiveBalances: (Collection<BalanceModel>) -> Unit): Single<ModelWrapper<UserTransactionModel?>>
}
