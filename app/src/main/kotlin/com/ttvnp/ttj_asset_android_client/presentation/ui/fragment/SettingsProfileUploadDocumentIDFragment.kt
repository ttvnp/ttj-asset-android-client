package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileUploadDocumentIDPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileUploadDocumentIDPresenterTarget
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import java.io.File
import javax.inject.Inject

class SettingsProfileUploadDocumentIDFragment : BaseMainFragment(), SettingsProfileUploadDocumentIDPresenterTarget {

    @Inject
    lateinit var settingsProfileUploadDocumentIDPresenter: SettingsProfileUploadDocumentIDPresenter

    private val cameraRequest = 9
    private var pictureUri: Uri? = null
    private var isFacePhoto: Boolean = false
    private var facePhotoFile: File? = null
    private var addressFile: File? = null

    private lateinit var imageFacePhoto: ImageView
    private lateinit var imageAddress: ImageView
    private lateinit var frameFacePhoto: FrameLayout
    private lateinit var frameAddress: FrameLayout
    private lateinit var buttonSave: Button

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        settingsProfileUploadDocumentIDPresenter.init(this)
    }

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
        buttonSave = view.findViewById(R.id.button_save)
        buttonSave.setOnClickListener({
            settingsProfileUploadDocumentIDPresenter.uploadIdDocument(faceImageFile = facePhotoFile, addressImageFile = addressFile)
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

        settingsProfileUploadDocumentIDPresenter.setupDefault()
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
                        arrayOf("image/jpeg"),null
                )
                Picasso.with(this.context).load(resultUri).resize(800, 800).centerInside().into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let {
                            Handler(Looper.getMainLooper()).post(object : Runnable {
                                override fun run() {
                                    if (isFacePhoto) {
                                        facePhotoFile = createUploadFile(context, it)
                                        imageFacePhoto.setImageBitmap(it)
                                        hasPhotos()
                                        return
                                    }

                                    addressFile = createUploadFile(context, it)
                                    imageAddress.setImageBitmap(it)
                                    hasPhotos()
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

    override fun setDocumentID(idDocument1ImageURL: String, idDocument2ImageURL: String) {
        if (idDocument1ImageURL.isNotBlank()) Picasso.with(this.context).load(idDocument1ImageURL).into(imageFacePhoto)
        if (idDocument2ImageURL.isNotBlank()) Picasso.with(this.context).load(idDocument2ImageURL).into(imageAddress)
    }

    override fun showMessageOnUploadSuccessfullyCompleted() {
        Toast.makeText(
                this.context,
                getString(R.string.successfully_uploaded),
                Toast.LENGTH_SHORT
        ).show()
        fragmentManager.popBackStack()
    }

    private fun hasPhotos() {
        if (facePhotoFile != null && addressFile != null) {
            setEnableButton(true)
        }
    }

    private fun setEnableButton(value: Boolean) {
        var backgroundColorSubmitButton = R.color.md_grey_200
        var textColor = R.color.md_grey_500

        if (value) {
            backgroundColorSubmitButton = R.color.colorPrimary
            textColor = R.color.colorTextOnPrimary
        }

        buttonSave.isEnabled = value
        buttonSave.setBackgroundColor(
                ContextCompat.getColor(
                        context,
                        backgroundColorSubmitButton
                )
        )
        buttonSave.setTextColor(ContextCompat.getColor(context, textColor))
    }

}