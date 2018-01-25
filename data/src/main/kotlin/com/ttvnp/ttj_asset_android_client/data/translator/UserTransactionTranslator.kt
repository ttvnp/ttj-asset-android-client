package com.ttvnp.ttj_asset_android_client.data.translator

import com.ttvnp.ttj_asset_android_client.data.entity.UserTransactionEntity
import com.ttvnp.ttj_asset_android_client.domain.model.*

internal class UserTransactionTranslator : BaseTranslator<UserTransactionModel, UserTransactionEntity>() {

    override internal fun translate(entity: UserTransactionEntity?): UserTransactionModel? {
        if (entity == null) {
            return null
        }
        val transactionStatus = TransactionStatus.values().filter { entity.transactionStatus == it.rawValue }.firstOrNull()
                ?: return null
        val transactionType = TransactionType.values().filter { entity.transactionType == it.rawValue }.firstOrNull()
                ?: return null
        val assetType = AssetType.values().filter { entity.assetType == it.rawValue }.firstOrNull()
                ?: return null
        return UserTransactionModel(
                id = entity.id,
                loggedAt = entity.loggedAt,
                transactionStatus = transactionStatus,
                transactionType = transactionType,
                targetUserID = entity.targetUserID,
                targetUserEmailAddress = entity.targetUserEmailAddress,
                targetUserProfileImageID = entity.targetUserProfileImageID,
                targetUserProfileImageURL = entity.targetUserProfileImageURL,
                targetUserFirstName = entity.targetUserFirstName,
                targetUserMiddleName = entity.targetUserMiddleName,
                targetUserLastName = entity.targetUserLastName,
                assetType = assetType,
                amount = entity.amount
        )
    }

    internal fun translateUserTransactions(entities: Collection<UserTransactionEntity>, hasMore: Boolean): UserTransactionsModel {
        val models = translate(entities)
        return UserTransactionsModel(models, hasMore)
    }
}
