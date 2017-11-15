package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.BalanceDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.BalanceTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.BalanceModel
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

    override fun updateBalances(balanceModels: Collection<BalanceModel>): Single<BalancesModel> {
        return Single.create { subscriber ->
            var balanceEntities: Collection<BalanceEntity> = arrayListOf<BalanceEntity>()
            balanceEntities = balanceEntities.toMutableList()
            for (bm in balanceModels) {
                balanceEntities.add(BalanceEntity(
                        assetType = bm.assetType.rawValue,
                        amount = bm.amount
                ))
            }
            balanceEntities = balanceDataStore.updates(balanceEntities)
            subscriber.onSuccess(BalanceTranslator().translateBalances(balanceEntities))
        }
    }
}