package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.BalanceDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.BalanceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.BalanceModel
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import com.ttvnp.ttj_asset_android_client.domain.util.addHour
import io.reactivex.Single
import java.io.IOException
import javax.inject.Inject

class BalanceRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val balanceDataStore : BalanceDataStore
) : BalanceRepository {

    override fun getBalances(forceRefresh: Boolean): Single<BalancesModel> {
        return Single.create { subscriber ->
            var balancesModel: BalancesModel? = null
            var refresh = forceRefresh
            if (!refresh) {
                val balanceEntities = balanceDataStore.getAll()
                if (balanceEntities.isEmpty()) {
                    refresh = true
                } else {
                    balanceEntities.forEach { balance ->
                        if (refresh) return@forEach
                        val localCacheExpiry = balance.updatedAt.addHour(24 * 7)
                        refresh = Now().after(localCacheExpiry)
                    }
                    if (!refresh) {
                        balancesModel = BalanceTranslator().translateBalances(balanceEntities)
                    }
                }
            }
            if (refresh) {
                try {
                    userService.getBalances().execute().body()?.let { response ->
                        if (response.hasError()) {
                            throw ServiceFailedException()
                        }
                        var balanceEntities: Collection<BalanceEntity> = arrayListOf()
                        balanceEntities = balanceEntities.toMutableList()

                        val targetAssetTypes = arrayListOf(AssetType.ASSET_TYPE_COIN, AssetType.ASSET_TYPE_POINT)
                        for (assetType in targetAssetTypes) {
                            val targetResponse = response.balances.filter {
                                it.assetType == assetType.rawValue
                            }.firstOrNull()
                            balanceEntities.add(BalanceEntity(
                                    assetType = assetType.rawValue,
                                    amount = targetResponse?.amount?:0,
                                    updatedAt = Now()
                            ))
                        }
                        balanceEntities = balanceDataStore.updates(balanceEntities)
                        balancesModel = BalanceTranslator().translateBalances(balanceEntities)
                    }
                } catch (e: IOException) {
                    // ignore connection exception.
                    if (balancesModel == null) {
                        // get from local db
                        balancesModel = BalanceTranslator().translateBalances(balanceDataStore.getAll())
                    }
                }
            }
            subscriber.onSuccess(balancesModel!!)
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