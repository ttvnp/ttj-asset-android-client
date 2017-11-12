package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.BalanceDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.BalanceTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import io.reactivex.Single
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val balanceDataStore : BalanceDataStore
) : BalanceRepository {

    override fun getBalances(): Single<BalancesModel> {
        return userService.getBalances().map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
            var balanceEntities: Collection<BalanceEntity> = arrayListOf()
            balanceEntities = balanceEntities.toMutableList()
            for (r in response.balances) {
                balanceEntities.add(BalanceEntity(
                        assetType = r.assetType,
                        amount = r.amount
                ))
            }
            balanceEntities = balanceDataStore.updates(balanceEntities)
            BalanceTranslator().translateBalances(balanceEntities)
        }.onErrorReturn { e ->
            val balanceEntities = balanceDataStore.getAll()
            BalanceTranslator().translateBalances(balanceEntities)
        }
    }
}