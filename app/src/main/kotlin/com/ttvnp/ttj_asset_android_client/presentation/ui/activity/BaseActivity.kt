package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.tracking.FirebaseAnalyticsHelper

abstract class BaseActivity : AppCompatActivity() {

    protected var progressDialog: Dialog? = null

    protected var firebaseAnalyticsHelper: FirebaseAnalyticsHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressDialog()
        firebaseAnalyticsHelper = FirebaseAnalyticsHelper(FirebaseAnalytics.getInstance(this))
    }

    protected fun initProgressDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.view_simple_progress)
        progressDialog = dialog
    }

    fun showProgressDialog() {
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    open fun showError(throwable: Throwable) {
        AlertDialog
                .Builder(this)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(resources.getString(R.string.error_default_message))
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }
}