package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import com.ttvnp.ttj_asset_android_client.domain.exceptions.BaseException
import com.ttvnp.ttj_asset_android_client.presentation.R

abstract class BaseMainFragment : Fragment() {

    protected var progressDialog: Dialog? = null

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

    fun showProgressDialog() {
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun showError(throwable: Throwable) {
        when (throwable) {
            is BaseException -> AlertDialog
                    .Builder(this.context)
                    .setTitle(resources.getString(R.string.error_dialog_title))
                    .setMessage(resources.getString(R.string.error_device_registration))
                    .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                    .show()
            else -> AlertDialog
                    .Builder(this.context)
                    .setTitle(resources.getString(R.string.error_dialog_title))
                    .setMessage(resources.getString(R.string.error_device_registration))
                    .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                    .show()
        }
    }
}