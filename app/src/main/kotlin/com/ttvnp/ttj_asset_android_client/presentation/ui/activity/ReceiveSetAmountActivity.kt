package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.ttvnp.ttj_asset_android_client.presentation.R

class ReceiveSetAmountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_set_amount)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_receive_amount)
        toolbar.title = getString(R.string.title_set_amount)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_grey)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }
}

