package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import androidx.core.content.ContextCompat
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.Gender
import com.ttvnp.ttj_asset_android_client.domain.model.IdentificationStatus
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.NationalCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.listener.getOnFocusChangeListener
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileEditPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileEditPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SettingsProfileEditFragment : BaseMainFragment(), SettingsProfileEditPresenterTarget {

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

    private val requestCode = 8
    private var isCamera = true
    private var pictureUri: Uri? = null
    private var profileImageFile: File? = null
    private lateinit var calendar: Calendar

    companion object {
        const val TMP_FILE_NAME = "tmp_profile_image"
        fun getInstance(): SettingsProfileEditFragment {
            return SettingsProfileEditFragment()
        }
    }

    override fun onAttach(context: Context) {
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
        textProfileEmailAddress.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.email_address))
        textInputLayoutProfileFirstName = view.findViewById(R.id.text_input_layout_profile_first_name)
        textProfileFirstName = view.findViewById(R.id.text_profile_first_name)
        textProfileFirstName.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.first_name))
        textInputLayoutProfileMiddleName = view.findViewById(R.id.text_input_layout_profile_middle_name)
        textProfileMiddleName = view.findViewById(R.id.text_profile_middle_name)
        textProfileMiddleName.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.middle_name))
        textInputLayoutProfileLastName = view.findViewById(R.id.text_input_layout_profile_last_name)
        textProfileLastName = view.findViewById(R.id.text_profile_last_name)
        textProfileLastName.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.last_name))
        textInputLayoutProfileAddress = view.findViewById(R.id.text_input_layout_profile_address)
        textProfileAddress = view.findViewById(R.id.text_profile_address)
        textProfileAddress.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.address_place_holder))
        radioGroupGender = view.findViewById(R.id.radio_group_gender)
        radioFemale = view.findViewById(R.id.radio_female)
        radioMale = view.findViewById(R.id.radio_male)
        textDOB = view.findViewById(R.id.text_dob)
        textProfileCellPhoneNumberNationalCode = view.findViewById(R.id.et_country_code)
        textProfileCellPhoneNumber = view.findViewById(R.id.et_phone_number)
        calendar = Calendar.getInstance()
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDOB()
        }
        textDOB.setOnClickListener {
            context?.let {
                DatePickerDialog(
                        it,
                        date,
                        1990,
                        0,
                        1)
                        .show()
            }
        }
        val buttonProfileSave = view.findViewById<Button>(R.id.button_profile_save)

        if (activity is SettingsProfileActivity) {
            val a = activity as SettingsProfileActivity
            a.toolbar.title = getString(R.string.title_settings_edit_profile)
            a.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            a.toolbar.setNavigationOnClickListener {
                if (0 < (fragmentManager?.backStackEntryCount ?: 0)) {
                    fragmentManager?.popBackStack()
                }
            }
        }

        bottomSheetDialogFragment = SettingsProfileEditBottomSheetDialogFragment.getInstance()
        bottomSheetDialogFragment.setFolderOnClickListener(View.OnClickListener {
            isCamera = false
            checkPermission(requestCode, isCamera)
        })
        bottomSheetDialogFragment.setCameraOnClickListener(View.OnClickListener {
            isCamera = true
            pictureUri = checkPermission(requestCode, isCamera)
        })
        buttonProfileImageEdit.setOnClickListener {
            bottomSheetDialogFragment.show(requireFragmentManager(), bottomSheetDialogFragment.tag)
        }

        buttonProfileSave.setOnClickListener {
            if (!validation()) {
                return@setOnClickListener
            }

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
        setEnableView(false)
        settingsProfileEditPresenter.setupUserInfo()

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsProfileEditPresenter.dispose()
    }

    override fun bindUserInfo(userModel: UserModel) {
        setEnableView(true)
        if (userModel.isIdentified) {
            setEnableView(false)
            context?.let { textDOB.setTextColor(ContextCompat.getColor(it, R.color.md_grey_400)) }
        }
        if (userModel.profileImageURL.isNotEmpty()) {
            context?.let {
                Picasso
                        .with(context)
                        .load(userModel.profileImageURL)
                        .into(profileImage)
            }
        }
        if (userModel.emailAddress.isNotBlank()) textProfileEmailAddress.text = userModel.emailAddress
        if (userModel.firstName.isNotBlank()) textProfileFirstName.setText(userModel.firstName)
        if (userModel.middleName.isNotBlank()) textProfileMiddleName.setText(userModel.middleName)
        if (userModel.lastName.isNotBlank()) textProfileLastName.setText(userModel.lastName)
        if (userModel.address.isNotBlank()) textProfileAddress.setText(userModel.address)
        if (userModel.dateOfBirth.isNotBlank()) textDOB.text = userModel.dateOfBirth
        when {
            userModel.genderType.type == Gender.FEMALE.rawValue -> radioFemale.isChecked = true
            userModel.genderType.type == Gender.MALE.rawValue -> radioMale.isChecked = true
        }
        if (userModel.phoneNumber.nationalCode.isNotBlank()) textProfileCellPhoneNumberNationalCode.setText(userModel.phoneNumber.nationalCode)
        if (userModel.phoneNumber.cellphoneNumber.isNotBlank()) textProfileCellPhoneNumber.setText(userModel.phoneNumber.cellphoneNumber)

        checkUnderReviewing(userModel.identificationStatus.rawValue)
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

        activity?.onBackPressed()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode != this.requestCode) return
        val uri = (if (pictureUri != null) {
            pictureUri
        } else {
            data?.data
        }) ?: return
        val imageRequiredSize = 72
        decodeUri(uri = uri, requiredSize = imageRequiredSize)?.let {
            val bitmap = getResultImage(it, getPath(uri = uri))
            profileImageFile = createUploadFile(context, bitmap, TMP_FILE_NAME)
            context?.let { c ->
                Glide.with(c).load(uri).into(profileImage)
            }

            // close modal
            if (!bottomSheetDialogFragment.isHidden) {
                bottomSheetDialogFragment.dismiss()
                pictureUri = null
            }
        }
    }

    private fun getGender(): Int {
        val identified = 0
        val selectGender = radioGroupGender.checkedRadioButtonId
        val radioButton: RadioButton = radioGroupGender.findViewById(selectGender)
        if (radioButton.text == getString(R.string.male)) {
            return Gender.MALE.rawValue
        } else if (radioButton.text == getString(R.string.female)) {
            return Gender.FEMALE.rawValue
        }

        return identified
    }

    private fun checkUnderReviewing(status: Int) {
        if (status == IdentificationStatus.Applied.rawValue) {
            var view: View? = null
            context?.let {
                val textColor = ContextCompat.getColor(it, R.color.md_grey_400)
                textProfileFirstName.setTextColor(textColor)
                textProfileFirstName.setOnFocusChangeListener { v, _ ->
                    clearUnderReviewMessage(view)
                    view = v
                }
                textProfileMiddleName.setTextColor(textColor)
                textProfileMiddleName.setOnFocusChangeListener { v, _ ->
                    clearUnderReviewMessage(view)
                    view = v
                }
                textProfileLastName.setTextColor(textColor)
                textProfileLastName.setOnFocusChangeListener { v, _ ->
                    clearUnderReviewMessage(view)
                    view = v
                }
                textProfileAddress.setTextColor(textColor)
                textProfileAddress.setOnFocusChangeListener { v, _ ->
                    clearUnderReviewMessage(view)
                    view = v
                }
                textProfileCellPhoneNumberNationalCode.setTextColor(textColor)
                textProfileCellPhoneNumberNationalCode.setOnFocusChangeListener { _, _ ->
                    clearUnderReviewMessage(view)
                    textProfileCellPhoneNumberNationalCode.error = null
                    textProfileCellPhoneNumber.error = getString(R.string.message_under_review)
                    textProfileCellPhoneNumber.requestFocus()
                    view = textProfileCellPhoneNumber
                }
                textProfileCellPhoneNumber.setTextColor(textColor)
                textProfileCellPhoneNumber.setOnFocusChangeListener { v, _ ->
                    clearUnderReviewMessage(view)
                    textProfileCellPhoneNumber.error = getString(R.string.message_under_review)
                    view = v
                }
                radioGroupGender.setOnClickListener {
                    clearUnderReviewMessage(view)
                    Toast.makeText(context, getString(R.string.message_under_review), Toast.LENGTH_SHORT).show()
                }
                radioFemale.isEnabled = false
                radioMale.isEnabled = false
                textDOB.setTextColor(textColor)
                textDOB.setOnClickListener {
                    clearUnderReviewMessage(view)
                    textDOB.requestFocus()
                    Toast.makeText(context, getString(R.string.message_under_review), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setEnableView(enable: Boolean) {
        textProfileFirstName.isEnabled = enable
        textProfileMiddleName.isEnabled = enable
        textProfileLastName.isEnabled = enable
        textProfileAddress.isEnabled = enable
        textDOB.isEnabled = enable
        radioMale.isEnabled = enable
        radioFemale.isEnabled = enable
        textProfileCellPhoneNumberNationalCode.isEnabled = enable
        textProfileCellPhoneNumber.isEnabled = enable
    }

    private fun clearUnderReviewMessage(view: View?) {
        view?.let {
            if (view is EditText) {
                view.error = null
            }
        }
    }

    private fun onFocusChangeListener(onFocusChanged: (view: View) -> Unit): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { view, _ ->
            if (view is EditText) {
                view.setRawInputType(InputType.TYPE_NULL)
                view.error = getString(R.string.message_under_review)
            }
            onFocusChanged(view)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDOB() {
        val format = "dd/MMM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        textDOB.text = sdf.format(calendar.time)
    }

    private fun validation(): Boolean {
        if (!validationNationalCode()) {
            return false
        }

        if (!radioFemale.isChecked && !radioMale.isChecked) {
            Toast.makeText(context, getString(R.string.please_select_gender), Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun validationNationalCode(): Boolean {
        val nationalCode: String = textProfileCellPhoneNumberNationalCode.text.toString()
        if (nationalCode == NationalCode.JAPAN.value || nationalCode == NationalCode.VIETNAM.value) {
            return true
        }

        Toast.makeText(
                context,
                R.string.national_code_should_be,
                Toast.LENGTH_SHORT
        ).show()
        return false
    }

}