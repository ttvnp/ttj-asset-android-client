package com.ttvnp.ttj_asset_android_client.presentation.ui.data

import android.content.Intent
import java.io.Serializable

data class PushNotificationBridgeData(
        val notificationType: String = "",
        val messageKey: String = "",
        val messageArgs1: String? = null,
        val messageArgs2: String? = null,
        val messageArgs3: String? = null
) : Serializable {

    companion object {
        val INTENT_KEY = "notified_object"
    }

    fun getMessageArgs(): Array<String> {
        return arrayListOf<String?>(messageArgs1, messageArgs2, messageArgs3).filter { s ->
            !s.isNullOrEmpty()
        }.map { s ->
            s!!
        }.toTypedArray()
    }
}

class PushNotificationBridgeDataFactory {
    companion object {
        val NOTIFICATION_TYPE_KEY = "notificationType"
        private val MESSAGE_KEY = "messageKey"
        private val MESSAGE_ARGS_KEY1 = "messageArgs1"
        private val MESSAGE_ARGS_KEY2 = "messageArgs2"
        private val MESSAGE_ARGS_KEY3 = "messageArgs3"

        fun create(intent: Intent): PushNotificationBridgeData {
            val notificationType: String = intent.getStringExtra(NOTIFICATION_TYPE_KEY)?:""
            val messageKey: String = intent.getStringExtra(MESSAGE_KEY)?:""
            val messageArgs1: String = intent.getStringExtra(MESSAGE_ARGS_KEY1)?:""
            val messageArgs2: String = intent.getStringExtra(MESSAGE_ARGS_KEY2)?:""
            val messageArgs3: String = intent.getStringExtra(MESSAGE_ARGS_KEY3)?:""
            return PushNotificationBridgeData(notificationType, messageKey, messageArgs1, messageArgs2, messageArgs3)
        }

        fun create(data: Map<String, String>): PushNotificationBridgeData {
            val notificationType: String = data.get(NOTIFICATION_TYPE_KEY)?:""
            val messageKey: String = data.get(MESSAGE_KEY)?:""
            val messageArgs1: String = data.get(MESSAGE_ARGS_KEY1)?:""
            val messageArgs2: String = data.get(MESSAGE_ARGS_KEY2)?:""
            val messageArgs3: String = data.get(MESSAGE_ARGS_KEY3)?:""
            return PushNotificationBridgeData(notificationType, messageKey, messageArgs1, messageArgs2, messageArgs3)
        }
    }
}

