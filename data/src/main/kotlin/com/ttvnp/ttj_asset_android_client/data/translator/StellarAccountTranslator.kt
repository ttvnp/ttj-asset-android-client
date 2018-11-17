package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.StellarAccountEntity
import com.ttvnp.ttj_asset_android_client.domain.model.StellarAccountModel

internal class StellarAccountTranslator : BaseTranslator<StellarAccountModel, StellarAccountEntity>() {

    override fun translate(entity: StellarAccountEntity?): StellarAccountModel? {
        if (entity == null) return null
        return StellarAccountModel(
                entity.strAccountID,
                entity.strDepositMemoText
        )
    }

}