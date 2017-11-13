package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.BalanceModel
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel

internal class BalanceTranslator : BaseTranslator<BalanceModel, BalanceEntity>() {

    override internal fun translate(entity: BalanceEntity?): BalanceModel? {
        if (entity == null) {
            return null
        }
        when (entity.assetType) {
            AssetType.ASSET_TYPE_POINT.rawValue -> return BalanceModel(AssetType.ASSET_TYPE_POINT, entity.amount)
            AssetType.ASSET_TYPE_COIN.rawValue -> return BalanceModel(AssetType.ASSET_TYPE_COIN, entity.amount)
            else -> return null
        }
    }

    internal fun translateBalances(entities: Collection<BalanceEntity>): BalancesModel {
        var point = BalanceModel(
                assetType = AssetType.ASSET_TYPE_POINT,
                amount = 0L
        )
        var coin = BalanceModel(
                assetType = AssetType.ASSET_TYPE_COIN,
                amount = 0L
        )
        for (e in entities) {
            when (e.assetType) {
                point.assetType.rawValue -> point = BalanceModel(point.assetType, e.amount)
                coin.assetType.rawValue -> coin = BalanceModel(coin.assetType, e.amount)
            }
        }
        return BalancesModel(point, coin)
    }
}