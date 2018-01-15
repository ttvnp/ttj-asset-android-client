package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity

class SettingsProfileUploadDocumentIDFragment : Fragment() {

    private var pictureUri: Uri? = null
    private var image: Int? = null

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
            image = 0
            checkCameraPermission()
        })
        frameAddress = view.findViewById(R.id.frame_address)
        frameAddress.setOnClickListener({
            image = 1
            checkCameraPermission()
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
            9 -> {
                if (resultCode != Activity.RESULT_OK) return
                val resultUri: Uri = (if (data == null) {
                    this.pictureUri
                } else {
                    data.data
                }) ?: return
                MediaScannerConnection.scanFile(
                        this.context,
                        arrayOf(resultUri.path),
                        arrayOf("image/jpeg"), null
                )
                Picasso.with(this.context).load(resultUri).resize(125, 125).centerCrop().into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let {
                            Handler(Looper.getMainLooper()).post(object : Runnable {
                                override fun run() {
                                    if (image == 0) {
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

    private fun checkCameraPermission() {
        if (hasSelfPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            launchCamera()
        } else {
            val permissions = arrayListOf<String>()
            if (!hasSelfPermissions(Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA)
            }
            if (!hasSelfPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            requestPermissions(permissions.toTypedArray(), 9)
        }
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        pictureUri = this.context
                .contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        startActivityForResult(intent, 9)
    }

    private fun hasSelfPermissions(vararg permissions: String): Boolean {
        return permissions.none { ActivityCompat.checkSelfPermission(this.context, it) != PackageManager.PERMISSION_GRANTED }
    }

}