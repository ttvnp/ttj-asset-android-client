package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileEditPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileEditPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject
import android.media.MediaScannerConnection
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.provider.MediaStore
import android.widget.Button
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.support.v4.app.ActivityCompat
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Target
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SettingsProfileEditFragment() : BaseMainFragment(), SettingsProfileEditPresenterTarget {

    @Inject
    lateinit var settingsProfileEditPresenter: SettingsProfileEditPresenter

    private lateinit var profileImage: CircleImageView
    private lateinit var buttonProfileImageEdit: FloatingActionButton
    private lateinit var textProfileEmailAddress: TextView
    private lateinit var textInputLayoutProfileFirstName: TextInputLayout
    private lateinit var textProfileFirstName: TextInputEditText
    private lateinit var textInputLayoutProfileMiddleName: TextInputLayout
    private lateinit var textProfileMiddleName: TextInputEditText
    private lateinit var textInputLayoutProfileLastName: TextInputLayout
    private lateinit var textProfileLastName: TextInputEditText
    private lateinit var textInputLayoutProfileAddress: TextInputLayout
    private lateinit var textProfileAddress: TextInputEditText

    private lateinit var bottomSheetDialogFragment: SettingsProfileEditBottomSheetDialogFragment
    private var pictureUri: Uri? = null
    private var profileImageFile: File? = null

    companion object {
        val TMP_FILE_NAME = "tmp_profile_image"
        fun getInstance() : SettingsProfileEditFragment {
            return SettingsProfileEditFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        settingsProfileEditPresenter.initialize(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_settings_profile_edit, container, false)
        profileImage = view.findViewById(R.id.profile_image)
        buttonProfileImageEdit = view.findViewById(R.id.button_profile_image_edit)
        textProfileEmailAddress = view.findViewById(R.id.text_profile_email_address)
        textInputLayoutProfileFirstName = view.findViewById(R.id.text_input_layout_profile_first_name)
        textProfileFirstName = view.findViewById(R.id.text_profile_first_name)
        textInputLayoutProfileMiddleName = view.findViewById(R.id.text_input_layout_profile_middle_name)
        textProfileMiddleName = view.findViewById(R.id.text_profile_middle_name)
        textInputLayoutProfileLastName = view.findViewById(R.id.text_input_layout_profile_last_name)
        textProfileLastName = view.findViewById(R.id.text_profile_last_name)
        textInputLayoutProfileAddress = view.findViewById(R.id.text_input_layout_profile_address)
        textProfileAddress = view.findViewById(R.id.text_profile_address)
        val buttonProfileSave = view.findViewById<Button>(R.id.button_profile_save)

        if (activity is SettingsProfileActivity) {
            val a = activity as SettingsProfileActivity
            a.toolbar.title = getString(R.string.title_settings_edit_profile)
            a.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            a.toolbar.setNavigationOnClickListener {
                if (0 < fragmentManager.backStackEntryCount) {
                    fragmentManager.popBackStack()
                }
            }
        }

        bottomSheetDialogFragment = SettingsProfileEditBottomSheetDialogFragment.getInstance()
        bottomSheetDialogFragment.setFolderOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("image/jpeg")
                startActivityForResult(intent, RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue)
            }
        })
        bottomSheetDialogFragment.setCameraOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
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
                    requestPermissions(permissions.toTypedArray(), RequestCode.REQUEST_PERMISSIONS.rawValue);
                }
            }
        })
        buttonProfileImageEdit.setOnClickListener {
            bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.getTag());
        }

        buttonProfileSave.setOnClickListener {
            settingsProfileEditPresenter.updateUserInfo(
                    profileImageFile,
                    textProfileFirstName.text.toString(),
                    textProfileMiddleName.text.toString(),
                    textProfileLastName.text.toString(),
                    textProfileAddress.text.toString()
            )
        }
        settingsProfileEditPresenter.setupUserInfo()

        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        if (0 < userModel.profileImageURL.length) {
            Picasso.with(this.context).load(userModel.profileImageURL).into(profileImage)
        }
        textProfileEmailAddress.setText(userModel.emailAddress)
        textProfileFirstName.setText(userModel.firstName)
        textProfileMiddleName.setText(userModel.middleName)
        textProfileLastName.setText(userModel.lastName)
        textProfileAddress.setText(userModel.address)
    }

    override fun showMessageOnUpdateSuccessfullyCompleted() {
        Toast.makeText(
                this.context,
                getString(R.string.profile_successfully_updated),
                Toast.LENGTH_SHORT
        ).show()
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        pictureUri = this.context
                .contentResolver
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        startActivityForResult(intent, RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue)
    }

    private fun hasSelfPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(this.context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun checkGrantResults(grantResults: Collection<Int>): Boolean {
        if (grantResults.size <= 0) throw IllegalArgumentException("grantResults is empty.")
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestCode.REQUEST_PERMISSIONS.rawValue -> {
                if (grantResults.size > 0) {
                    if (checkGrantResults(grantResults.toList())) {
                        launchCamera()
                    } else {
                        Toast.makeText(
                                this.context,
                                getString(R.string.error_permission_denied_on_launch_camera),
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue -> {
                if (resultCode != RESULT_OK) return
                val resultUri: Uri?
                if (data == null) {
                    resultUri = this.pictureUri
                } else {
                    resultUri = data.data
                }
                if (resultUri == null) {
                    return
                }
                MediaScannerConnection.scanFile(
                        this.context,
                        arrayOf(resultUri.path),
                        arrayOf("image/jpeg"), null
                )
                Picasso.with(this.context).load(resultUri).resize(72, 72).centerCrop().into(object : Target{
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let {
                            Handler(Looper.getMainLooper()).post(object : Runnable{
                                override fun run() {
                                    profileImageFile = createUploadFile(this@SettingsProfileEditFragment.context, it)
                                    profileImage.setImageBitmap(it)
                                }
                            })
                        }
                    }
                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    }
                    override fun onBitmapFailed(errorDrawable: Drawable?) {
                        profileImageFile = null
                    }
                })
                // close modal
                if (!bottomSheetDialogFragment.isHidden) {
                    bottomSheetDialogFragment.dismiss()
                }
            }
        }
    }

    private fun createUploadFile(context: Context, bitmap: Bitmap): File {
        val file = File(context.externalCacheDir, TMP_FILE_NAME)
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
}