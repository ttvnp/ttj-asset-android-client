package com.ttvnp.ttj_asset_android_client.data.service.response

class StellarAccountResponse(
        val id: String = "",
        val account_id: String = "",
        val balances: Array<StellarBalanceResponse>
)

class StellarBalanceResponse(
        val blanace: String?,
        val limit: String?,
        val asset_type: String?,
        val asset_code: String?,
        val asset_issuer: String?
)