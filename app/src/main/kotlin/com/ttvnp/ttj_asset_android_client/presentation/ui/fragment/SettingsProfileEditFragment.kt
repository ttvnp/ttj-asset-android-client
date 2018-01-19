package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileEditPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileEditPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SettingsProfileEditFragment : BaseFragment(), SettingsProfileEditPresenterTarget {

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
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioMale: RadioButton
    private lateinit var radioFemale: RadioButton
    private lateinit var textDOB: TextView
    private lateinit var textProfileCellPhoneNumberNationalCode: EditText
    private lateinit var textProfileCellPhoneNumber: EditText
    private lateinit var bottomSheetDialogFragment: SettingsProfileEditBottomSheetDialogFragment

    private var pictureUri: Uri? = null
    private var profileImageFile: File? = null
    private lateinit var calendar: Calendar

    companion object {
        val FEMALE = 1
        val MALE = 2
        val TMP_FILE_NAME = "tmp_profile_image"
        fun getInstance(): SettingsProfileEditFragment {
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
    ): View {
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
        radioGroupGender = view.findViewById(R.id.radio_group_gender)
        radioFemale = view.findViewById(R.id.radio_female)
        radioMale = view.findViewById(R.id.radio_male)
        textDOB = view.findViewById(R.id.text_dob)
        textProfileCellPhoneNumberNationalCode = view.findViewById(R.id.et_country_code)
        textProfileCellPhoneNumber = view.findViewById(R.id.et_phone_number)
        calendar = Calendar.getInstance()
        updateDOB()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDOB()
        }
        textDOB.setOnClickListener({
            DatePickerDialog(
                    context,
                    date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
                    .show()
        })
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
        bottomSheetDialogFragment.setFolderOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/jpeg"
            startActivityForResult(intent, RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue)
        })
        bottomSheetDialogFragment.setCameraOnClickListener(View.OnClickListener {
            pictureUri = checkCameraPermission(RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue)
        })
        buttonProfileImageEdit.setOnClickListener {
            bottomSheetDialogFragment.show(fragmentManager, bottomSheetDialogFragment.tag)
        }

        buttonProfileSave.setOnClickListener {
            settingsProfileEditPresenter.updateUserInfo(
                    profileImageFile,
                    textProfileFirstName.text.toString(),
                    textProfileMiddleName.text.toString(),
                    textProfileLastName.text.toString(),
                    textProfileAddress.text.toString(),
                    getGender(),
                    textDOB.text.toString(),
                    textProfileCellPhoneNumberNationalCode.text.toString(),
                    textProfileCellPhoneNumber.text.toString()
            )
        }
        settingsProfileEditPresenter.setupUserInfo()

        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        if (userModel.profileImageURL.isNotEmpty()) {
            Picasso.with(this.context).load(userModel.profileImageURL).into(profileImage)
        }
        textProfileEmailAddress.text = userModel.emailAddress
        textProfileFirstName.setText(userModel.firstName)
        textProfileMiddleName.setText(userModel.middleName)
        textProfileLastName.setText(userModel.lastName)
        textProfileAddress.setText(userModel.address)
        if (userModel.dateOfBirth.isNotBlank()) textDOB.text = userModel.dateOfBirth
        when {
            userModel.genderType == FEMALE -> radioFemale.isChecked = true
            userModel.genderType == MALE -> radioMale.isChecked = true
            else -> radioMale.isChecked = true
        }
        textProfileCellPhoneNumberNationalCode.setText(userModel.cellphoneNumberNationalCode)
        textProfileCellPhoneNumber.setText(userModel.cellphoneNumber)
    }

    override fun showMessageOnUpdateSuccessfullyCompleted() {
        Toast.makeText(
                this.context,
                getString(R.string.profile_successfully_updated),
                Toast.LENGTH_SHORT
        ).show()
        if (profileImageFile != null) {
            firebaseAnalyticsHelper?.setHasSetProfileImageUserPropertyOn()
        }
    }

    private fun getGender(): Int {
        val identified = 0
        val selectGender = radioGroupGender.checkedRadioButtonId
        val radioButton: RadioButton = radioGroupGender.findViewById(selectGender)
        if (radioButton.text == getString(R.string.male)) {
            return MALE
        } else if (radioButton.text == getString(R.string.female)) {
            return FEMALE
        }

        return identified
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDOB() {
        val format = "dd/MMM/yyyy"
        val sdf = SimpleDateFormat(format)
        textDOB.text = sdf.format(calendar.time)
    }

    private fun checkGrantResults(grantResults: Collection<Int>): Boolean {
        if (grantResults.isEmpty()) throw IllegalArgumentException("grantResults is empty.")
        return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RequestCode.REQUEST_PERMISSIONS.rawValue -> {
                if (grantResults.isNotEmpty()) {
                    if (checkGrantResults(grantResults.toList())) {
                        launchCamera(RequestCode.IMAGE_SELECTOR_ACTIVITY.rawValue)
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
                Picasso.with(this.context).load(resultUri).resize(72, 72).centerCrop().into(object : Target {
                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                        bitmap?.let {
                            Handler(Looper.getMainLooper()).post(object : Runnable {
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

}