package com.ttvnp.ttj_asset_android_client.data.driver

import android.content.Context
import android.util.Log
import com.github.gfx.android.orma.AccessThreadConstraint
import com.github.gfx.android.orma.migration.ManualStepMigration
import com.ttvnp.ttj_asset_android_client.data.entity.OrmaDatabase
import javax.inject.Inject


class OrmaHolder @Inject constructor(
        val context: Context
) {
    private val VERSION_1: Int = 1
    val ormaDatabase = OrmaDatabase.builder(context)
            // .name("ttj_asset_android_client.db")
            .migrationStep(VERSION_1, object : ManualStepMigration.ChangeStep() {
                override fun change(helper: ManualStepMigration.Helper) {
                    Log.d("OrmaHolder", if (helper.upgrade) "upgrade" else "downgrade")
                }
            })
            .writeOnMainThread(AccessThreadConstraint.NONE)
            .readOnMainThread(AccessThreadConstraint.NONE)
            .build()
}
