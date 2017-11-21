package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.tracking.FirebaseAnalyticsHelper

abstract class BaseFragment : Fragment() {

    protected var progressDialog: Dialog? = null

    protected var firebaseAnalyticsHelper: FirebaseAnalyticsHelper? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        firebaseAnalyticsHelper = FirebaseAnalyticsHelper(FirebaseAnalytics.getInstance(this.context))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initProgressDialog()
    }

    protected fun initProgressDialog() {
        val dialog = Dialog(this.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.view_simple_progress)
        progressDialog = dialog
    }

    open fun showProgressDialog() {
        progressDialog?.show()
    }

    open fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    open fun showError(throwable: Throwable) {
        AlertDialog
                .Builder(this.context)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(resources.getString(R.string.error_default_message))
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }

    open fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        AlertDialog
                .Builder(this.context)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(resources.getString(R.string.error_default_message))
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }
}