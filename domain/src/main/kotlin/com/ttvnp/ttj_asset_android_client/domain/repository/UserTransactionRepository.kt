package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.*
import io.reactivex.Single

interface UserTransactionRepository {
    fun getTopByUserID(upperID: Long, limit: Long, forceRefresh: Boolean): Single<UserTransactionsModel>
    fun createTransaction(sendInfoModel: SendInfoModel, password: String, onReceiveBalances: (Collection<BalanceModel>) -> Unit): Single<ModelWrapper<UserTransactionModel?>>
    fun createExternalTransaction(strAccountID: String, memoText: String, assetType: AssetType, amount: Long, password: String, onReceiveBalances: (Collection<BalanceModel>) -> Unit): Single<ModelWrapper<UserTransactionModel?>>
}
