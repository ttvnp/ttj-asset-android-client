package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsNotificationPresenter
import dagger.android.AndroidInjection
import javax.inject.Inject

class SettingsNotificationActivity : BaseActivity() {

    @Inject
    lateinit var settingsNotificationPresenter : SettingsNotificationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_notification)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_settings_notification)
        toolbar.title = getString(R.string.title_settings_notification)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent);
            finish()
        }
    }
}
