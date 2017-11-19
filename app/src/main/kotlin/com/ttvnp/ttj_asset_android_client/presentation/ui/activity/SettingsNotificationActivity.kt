package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Switch
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsNotificationPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsNotificationPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class SettingsNotificationActivity : BaseActivity(), SettingsNotificationPresenterTarget {

    @Inject
    lateinit var settingsNotificationPresenter : SettingsNotificationPresenter

    private lateinit var switchPushNotification: Switch
    private lateinit var switchEmailNotification: Switch

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

        switchPushNotification = findViewById(R.id.switch_push_notification)
        switchEmailNotification = findViewById(R.id.switch_email_notification)
        switchPushNotification.setOnClickListener {
            settingsNotificationPresenter.updateGrantPushNotification(switchPushNotification.isChecked)
        }
        switchEmailNotification.setOnClickListener {
            settingsNotificationPresenter.updateGrantEmailNotification(switchEmailNotification.isChecked)
        }
        settingsNotificationPresenter.setupNotificationInfo(this)
    }

    override fun bindDeviceInfo(deviceModel: DeviceModel) {
        switchPushNotification.isChecked = deviceModel.grantPushNotification
        switchEmailNotification.isChecked = deviceModel.grantEmailNotification
    }
}
