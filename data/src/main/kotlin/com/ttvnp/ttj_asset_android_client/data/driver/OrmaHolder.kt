package com.ttvnp.ttj_asset_android_client.data.driver

import android.content.Context
import com.github.gfx.android.orma.AccessThreadConstraint
import com.ttvnp.ttj_asset_android_client.data.entity.OrmaDatabase
import javax.inject.Inject


class OrmaHolder @Inject constructor(
        val context: Context
) {
    val ormaDatabase = OrmaDatabase.builder(context)
            .writeOnMainThread(AccessThreadConstraint.NONE)
            .readOnMainThread(AccessThreadConstraint.NONE)
            .build()
}
