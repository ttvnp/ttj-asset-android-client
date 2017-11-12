package com.ttvnp.ttj_asset_android_client.domain.repository

import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import io.reactivex.Single

interface BalanceRepository {
    fun getBalances(): Single<BalancesModel>
}