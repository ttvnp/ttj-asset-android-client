package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.BalanceEntity
import com.ttvnp.ttj_asset_android_client.domain.model.BalanceModel
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel

internal class BalanceTranslator : BaseTranslator<BalanceModel, BalanceEntity>() {

    override internal fun translate(entity: BalanceEntity?): BalanceModel? {
        if (entity == null) {
            return null
        }
        return BalanceModel(
                entity.assetType,
                entity.amount
        )
    }

    internal fun translateBalances(entities: Collection<BalanceEntity>): BalancesModel {
        var point = BalanceModel(
                assetType = BalanceModel.ASSET_TYPE_POINT,
                amount = 0L
        )
        var coin = BalanceModel(
                assetType = BalanceModel.ASSET_TYPE_COIN,
                amount = 0L
        )
        for (e in entities) {
            when (e.assetType) {
                point.assetType -> point = BalanceModel(e.assetType, e.amount)
                coin.assetType -> coin = BalanceModel(e.assetType, e.amount)
            }
        }
        return BalancesModel(point, coin)
    }
}