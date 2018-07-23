package com.ttvnp.ttj_asset_android_client.presentation.ui.tracking

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType

class FirebaseAnalyticsHelper(val firebaseAnalytics: FirebaseAnalytics) {

    fun logTutorialBeginEvent() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, bundle)
    }

    fun logTutorialCompleteEvent() {
        val bundle = Bundle()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE, bundle)
    }

    fun logAssetSendEvent(assetType: AssetType, amount: Long) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, assetType.rawValue)
        bundle.putString(FirebaseAnalytics.Param.VIRTUAL_CURRENCY_NAME, assetType.rawValue)
        bundle.putLong(FirebaseAnalytics.Param.VALUE, amount)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SPEND_VIRTUAL_CURRENCY, bundle)
    }

    fun setHasSetProfileImageUserPropertyOn() {
        firebaseAnalytics.setUserProperty("has_set_profile_image", "1");
    }
}
