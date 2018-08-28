package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileUploadDocumentIDPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileUploadDocumentIDPresenterTarget
import dagger.android.support.AndroidSupportInjection
import java.io.File
import javax.inject.Inject

class SettingsProfileUploadDocumentIDFragment : BaseMainFragment(), SettingsProfileUploadDocumentIDPresenterTarget {

    @Inject
    lateinit var settingsProfileUploadDocumentIDPresenter: SettingsProfileUploadDocumentIDPresenter

    private val requestCode = 9
    private var isCamera = true
    private var isFacePhoto: Boolean = false
    private var pictureUri: Uri? = null
    private var facePhotoFile: File? = null
    private var addressFile: File? = null
    private var userModel: UserModel? = null

    private lateinit var imageFacePhoto: ImageView
    private lateinit var imageAddress: ImageView
    private lateinit var frameFacePhoto: FrameLayout
    private lateinit var frameAddress: FrameLayout
    private lateinit var buttonSave: Button
    private lateinit var bottomSheetDialogFragment: SettingsProfileEditBottomSheetDialogFragment

    companion object {
        val TMP_FILE_NAME_FACE = "tmp_id_face_image"
        val TMP_FILE_NAME_ADDRESS = "tmp_id_address_image"
    }

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
        frameFacePhoto = view.findViewById(R.id.frame_face_photo)
        frameFacePhoto.setOnClickListener({
            isFacePhoto = true
            bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.tag)
        })

        imageAddress = view.findViewById(R.id.image_address)
        frameAddress = view.findViewById(R.id.frame_address)
        frameAddress.setOnClickListener({
            isFacePhoto = false
            bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.tag)
        })
        buttonSave = view.findViewById(R.id.button_save)
        buttonSave.setOnClickListener({
            userModel?.let {
                if (!it.hasAllNecessaryInfo(context)) {
                    AlertDialog
                            .Builder(context)
                            .setMessage(getString(R.string.validate_profile_for_id_document))
                            .setPositiveButton(android.R.string.yes, { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            })
                            .show()
                    return@setOnClickListener
                }
                settingsProfileUploadDocumentIDPresenter.uploadIdDocument(faceImageFile = facePhotoFile, addressImageFile = addressFile)
            }
        })

        bottomSheetDialogFragment = SettingsProfileEditBottomSheetDialogFragment.getInstance()
        bottomSheetDialogFragment.setFolderOnClickListener(View.OnClickListener {
            isCamera = false
            checkPermission(requestCode,isCamera)
        })
        bottomSheetDialogFragment.setCameraOnClickListener(View.OnClickListener {
            isCamera = true
            pictureUri = checkPermission(requestCode, isCamera)
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
        if (resultCode != Activity.RESULT_OK) return
        val uri = (if (pictureUri != null) {
            pictureUri
        } else {
            data?.data
        }) ?: return

        if (!bottomSheetDialogFragment.isHidden) {
            bottomSheetDialogFragment.dismiss()
            pictureUri = null
        }

        val imageRequiredSize = 800
        val bitmap = getResultImage(decodeUri(uri = uri, requiredSize = imageRequiredSize), getPath(uri = uri))
        if (isFacePhoto) {
            facePhotoFile = createUploadFile(context, bitmap, TMP_FILE_NAME_FACE)
            Glide.with(context).load(uri).into(imageFacePhoto)
            hasPhotos()
            return
        }

        addressFile = createUploadFile(context, bitmap, TMP_FILE_NAME_ADDRESS)
        Glide.with(context).load(uri).into(imageAddress)
        hasPhotos()
    }

    override fun setDocumentID(userModel: UserModel) {
        this.userModel = userModel
        if (userModel.isDocument1ImageURL.isNotBlank()) Glide.with(this.context).load(userModel.isDocument1ImageURL).into(imageFacePhoto)
        if (userModel.isDocument2ImageURL.isNotBlank()) Glide.with(this.context).load(userModel.isDocument2ImageURL).into(imageAddress)
    }

    override fun showMessageOnUploadSuccessfullyCompleted() {
        Toast.makeText(
                this.context,
                getString(R.string.successfully_uploaded),
                Toast.LENGTH_SHORT
        ).show()
        fragmentManager.popBackStack()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestCode -> {
                if (grantResults.isEmpty()) return
                if (checkGrantResults(grantResults.toList())) {
                    if (isCamera) {
                        pictureUri = launchCamera(requestCode)
                        return
                    }

                    openGallery(requestCode)
                    return
                }

                Toast.makeText(
                        this.context,
                        getString(R.string.error_permission_denied),
                        Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                        buttonSave.context,
                        backgroundColorSubmitButton
                )
        )
        buttonSave.setTextColor(ContextCompat.getColor(buttonSave.context, textColor))
    }

}