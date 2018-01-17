package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity

class SettingsProfileUploadDocumentIDFragment : BaseFragment() {

    private val cameraRequest = 9
    private var pictureUri: Uri? = null
    private var isFacePhoto: Boolean? = null

    private lateinit var imageFacePhoto: ImageView
    private lateinit var imageAddress: ImageView
    private lateinit var frameFacePhoto: FrameLayout
    private lateinit var frameAddress: FrameLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?)
            : View? {
        val view = inflater.inflate(R.layout.fragment_settings_profile_upload_document_id, container, false)
        imageFacePhoto = view.findViewById(R.id.image_face_photo)
        imageAddress = view.findViewById(R.id.image_address)
        frameFacePhoto = view.findViewById(R.id.frame_face_photo)
        frameFacePhoto.setOnClickListener({
            isFacePhoto = true
            pictureUri = checkCameraPermission(cameraRequest)
        })
        frameAddress = view.findViewById(R.id.frame_address)
        frameAddress.setOnClickListener({
            isFacePhoto = false
            pictureUri = checkCameraPermission(cameraRequest)
        })

        if (activity is SettingsProfileActivity) {
            val a = activity as SettingsProfileActivity
            a.toolbar.title = getString(R.string.upload_your_id_document)
            a.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            a.toolbar.setNavigationOnClickListener {
                if (0 < fragmentManager.backStackEntryCount) {
                    fragmentManager.popBackStack()
                }
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            cameraRequest -> {
                if (resultCode != Activity.RESULT_OK) return
                val resultUri: Uri = (if (data == null) {
                    this.pictureUri
                } else {
                    data.data
                }) ?: return
                MediaScannerConnection.scanFile(
                        this.context,
                        arrayOf(resultUri.path),
                        arrayOf("isFacePhoto/jpeg"), null
                )
                Picasso.with(this.context).load(resultUri).resize(125, 125).centerCrop().into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let {
                            Handler(Looper.getMainLooper()).post(object : Runnable {
                                override fun run() {
                                    if (isFacePhoto == true) {
                                        imageFacePhoto.setImageBitmap(it)
                                        return
                                    }

                                    imageAddress.setImageBitmap(it)
                                }
                            })
                        }
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }

                    override fun onBitmapFailed(errorDrawable: Drawable?) {

                    }
                })
            }
        }
    }

}