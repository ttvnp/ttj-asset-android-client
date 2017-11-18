package com.ttvnp.ttj_asset_android_client.presentation.ui.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class AndroidFirebaseInstanceIDService : FirebaseInstanceIdService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().getToken()
        Log.d(javaClass.name, "Refreshed token: " + refreshedToken)
        // send to server
    }
}
