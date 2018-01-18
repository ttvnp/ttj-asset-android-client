package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.Window
import android.view.WindowManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crash.FirebaseCrash
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.error.ErrorMessageGenerator
import com.ttvnp.ttj_asset_android_client.presentation.ui.tracking.FirebaseAnalyticsHelper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

    protected fun launchCamera(requestCode: Int): Uri {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        val pictureUri = this.context
                .contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        startActivityForResult(intent, requestCode)

        return pictureUri
    }

    protected fun checkCameraPermission(requestCode: Int): Uri? {
        if (hasSelfPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return launchCamera(requestCode = requestCode)
        } else {
            val permissions = arrayListOf<String>()
            if (!hasSelfPermissions(Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA)
            }
            if (!hasSelfPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            requestPermissions(permissions.toTypedArray(), requestCode)
        }

        return null
    }

    protected fun createUploadFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.externalCacheDir, SettingsProfileEditFragment.TMP_FILE_NAME)
        var fos: FileOutputStream? = null
        try {
            file.createNewFile()
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: IOException) {
        } finally {
            try {
                fos?.let {
                    it.flush()
                    it.close()
                }
            } catch (e: IOException) {
            }
        }
        return file
    }

    private fun hasSelfPermissions(vararg permissions: String): Boolean {
        return permissions.none { ActivityCompat.checkSelfPermission(this.context, it) != PackageManager.PERMISSION_GRANTED }
    }

    open fun showProgressDialog() {
        progressDialog?.show()
    }

    open fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    protected open fun showErrorDialog(errorMessage: String) {
        AlertDialog
                .Builder(this.context)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(errorMessage)
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }

    protected open fun showErrorDialog(errorMessage: String, onClick: (dialog: DialogInterface?, whichButton: Int) -> Unit) {
        AlertDialog
                .Builder(this.context)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(errorMessage)
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), onClick)
                .show()
    }

    open fun showError(throwable: Throwable) {
        showErrorDialog(errorMessageGenerator.default())
        FirebaseCrash.report(throwable)
    }

    open fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        showErrorDialog(errorMessageGenerator.generate(errorCode, throwable))
    }
}