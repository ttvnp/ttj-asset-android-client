package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.NotificationType
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.PushNotificationBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.PushNotificationBridgeDataFactory

// This activity act as push notification hub.
class PushReceiveActivity : AppCompatActivity() {

    companion object {
        const val PUSH_NOTIFICATION_ACTION = "push_notification"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handle()
    }

    override fun onNewIntent(intent: Intent?) {
        handle()
    }

    private fun handle() {
        val data = intent.getSerializableExtra(PushNotificationBridgeData.INTENT_KEY)
        val bridgeData: PushNotificationBridgeData
        if (data == null || !(data is PushNotificationBridgeData)) {
            bridgeData = PushNotificationBridgeDataFactory.create(intent)
        } else {
            bridgeData = data
        }
        handleData(bridgeData)
    }

    private fun handleData(data: PushNotificationBridgeData) {
        when (data.notificationType) {
            NotificationType.NOTIFICATION_TYPE_RECEIVE_PAYMENT.rawValue -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(PushNotificationBridgeData.INTENT_KEY, data)
                startActivity(intent)
                finish()
            }
            else -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(PushNotificationBridgeData.INTENT_KEY, data)
                startActivity(intent)
                finish()
            }
        }
    }
}
