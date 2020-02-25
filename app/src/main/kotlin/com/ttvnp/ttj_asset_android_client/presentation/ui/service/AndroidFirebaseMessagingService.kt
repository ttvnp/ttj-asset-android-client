package com.ttvnp.ttj_asset_android_client.presentation.ui.service

import android.app.*
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.PushReceiveActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.PushNotificationBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.PushNotificationBridgeDataFactory
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.DeviceTokenUpdater
import dagger.android.AndroidInjection
import javax.inject.Inject


class AndroidFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var deviceTokenUpdater : DeviceTokenUpdater

    companion object {
        private val DEFAULT_CHANNEL_ID = "default_channel"
        private val DEFAULT_NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onNewToken(token: String?) {
        token?.let { deviceToken ->
            deviceTokenUpdater.updateDeviceToken(deviceToken)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        val clickAction: String = remoteMessage.notification?.clickAction?:""
        val targetActivity: Class<out Activity>
        when (clickAction) {
            PushReceiveActivity.PUSH_NOTIFICATION_ACTION -> targetActivity = PushReceiveActivity::class.java
            else -> targetActivity = PushReceiveActivity::class.java
        }

        val getStringResourceIDByName: (String?) -> Int? = { name ->
            var id: Int? = null
            try {
                name?.let {
                    val result = resources.getIdentifier(it, "string", packageName)
                    if (result != 0) id = result
                }
            } catch (t: Throwable) {
                FirebaseCrashlytics.getInstance().log("push: invalid resource key string ${name} was detected.")
            }
            id
        }
        val titleKeyID = getStringResourceIDByName(remoteMessage.notification?.titleLocalizationKey)?:R.string.any
        val titleArgs = remoteMessage.notification?.titleLocalizationArgs?: arrayOf(remoteMessage.notification?.title?:getString(R.string.app_name))
        var title = ""
        try {
            title = getString(titleKeyID, *titleArgs)
        } catch (e: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        val bodyKeyID = getStringResourceIDByName(remoteMessage.notification?.bodyLocalizationKey)?:R.string.any
        val bodyArgs = remoteMessage.notification?.bodyLocalizationArgs?: arrayOf(remoteMessage.notification?.body?:"")
        var body = ""
        try {
            body = getString(bodyKeyID, *bodyArgs)
        } catch (e: Throwable) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }

        val data = remoteMessage.data
        val bridgeData = PushNotificationBridgeDataFactory.create(data)

        // create notification.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder: NotificationCompat.Builder
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(DEFAULT_CHANNEL_ID, getString(R.string.channel_name_default), importance)
            notificationChannel.lightColor = R.color.colorPrimary
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(notificationChannel)
            builder = NotificationCompat.Builder(applicationContext, notificationChannel.id)
        } else {
            builder = NotificationCompat.Builder(applicationContext)
        }
        builder = builder
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorPrimary))
                .setContentTitle(title)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)

        // create intent used on tapped.
        val intent = Intent(this, targetActivity)
        intent.action = clickAction
        intent.putExtra(PushNotificationBridgeData.INTENT_KEY, bridgeData)
        val contentIntent = PendingIntent.getActivity(
                applicationContext,
                RequestCode.PUSH_RECEIVE_ACTIVITY.rawValue,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(contentIntent)

        notificationManager.notify(DEFAULT_NOTIFICATION_ID, builder.build())
    }
}