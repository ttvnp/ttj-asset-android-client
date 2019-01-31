package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.MaintenanceActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.error.ErrorMessageGenerator
import com.ttvnp.ttj_asset_android_client.presentation.ui.tracking.FirebaseAnalyticsHelper
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var errorMessageGenerator: ErrorMessageGenerator

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
        val dialog = Dialog(context)
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

    open fun showMaintenance() {
        MaintenanceActivity.start()
    }

    protected open fun showErrorDialog(errorMessage: String) {
        context?.let {
            AlertDialog
                    .Builder(it)
                    .setTitle(getString(R.string.error_dialog_title))
                    .setMessage(errorMessage)
                    .setPositiveButton(getString(R.string.default_positive_button_text), null)
                    .show()
        }
    }

    protected open fun showErrorDialog(errorMessage: String, onClick: (dialog: DialogInterface?, whichButton: Int) -> Unit) {
        context?.let {
            AlertDialog
                    .Builder(it)
                    .setTitle(getString(R.string.error_dialog_title))
                    .setMessage(errorMessage)
                    .setPositiveButton(getString(R.string.default_positive_button_text), onClick)
                    .show()
        }
    }

    open fun showError(throwable: Throwable) {
        if (throwable is UnknownHostException) {
            showErrorDialog(getString(R.string.error_cannot_connect_to_server))
        } else {
            showErrorDialog(getString(R.string.error_default_message))
        }
        FirebaseCrash.report(throwable)
    }

    open fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        showErrorDialog(getString(errorMessageGenerator.convert(errorCode)))
    }
}