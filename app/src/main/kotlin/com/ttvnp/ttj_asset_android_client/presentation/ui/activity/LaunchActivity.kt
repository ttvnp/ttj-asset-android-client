package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.error.ErrorMessageGenerator
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.LaunchPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.LaunchPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class LaunchActivity : Activity(), LaunchPresenterTarget {

    @Inject
    lateinit var errorMessageGenerator: ErrorMessageGenerator

    @Inject
    lateinit var launchPresenter : LaunchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        launchPresenter.initialize(this)

        // get user data
        launchPresenter.checkDeviceReady()
    }

    override fun startNextActivity(isDeviceReady: Boolean) {
        if (isDeviceReady) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showErrorDialog(errorMessage: String) {
        AlertDialog
                .Builder(this)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(errorMessage)
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }

    override fun showError(throwable: Throwable) {
        showErrorDialog(errorMessageGenerator.default())
    }

    override fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        showErrorDialog(errorMessageGenerator.generate(errorCode, throwable))
    }

    override fun showProgressDialog() {
        // don't use this method
        throw NotImplementedError()
    }

    override fun dismissProgressDialog() {
        // don't use this method
        throw NotImplementedError()
    }
}