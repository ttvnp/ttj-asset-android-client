package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import com.ttvnp.ttj_asset_android_client.presentation.AndroidApplication.Companion.context
import com.ttvnp.ttj_asset_android_client.presentation.R

class MaintenanceActivity : BaseActivity() {

    companion object {
        fun start() {
           context?.let {
               val intent = Intent(it, MaintenanceActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
               it.startActivity(intent)
           }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maintenance)
    }

}