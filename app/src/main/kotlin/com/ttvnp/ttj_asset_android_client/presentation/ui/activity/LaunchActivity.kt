package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.LaunchPresenter
import dagger.android.AndroidInjection
import javax.inject.Inject

class LaunchActivity : Activity() {

    @Inject
    lateinit var launchPresenter : LaunchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        // get user data
        launchPresenter.checkDeviceReady { isReady ->
            if (isReady) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, TutorialActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}