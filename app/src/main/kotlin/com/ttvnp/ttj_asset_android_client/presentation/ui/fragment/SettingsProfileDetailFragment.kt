package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ttvnp.ttj_asset_android_client.domain.model.IdentificationStatus
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileDetailPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileDetailPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class SettingsProfileDetailFragment : BaseFragment(), SettingsProfileDetailPresenterTarget {

    @Inject
    lateinit var settingsProfileDetailPresenter: SettingsProfileDetailPresenter

    private lateinit var profileImage: CircleImageView
    private lateinit var textProfileEmailAddress: TextView
    private lateinit var textProfileFirstName: TextView
    private lateinit var textProfileMiddleName: TextView
    private lateinit var textProfileLastName: TextView
    private lateinit var textProfileAddress: TextView
    private lateinit var textProfileGender: TextView
    private lateinit var textProfileDOB: TextView
    private lateinit var textProfileCellPhone: TextView
    private lateinit var buttonUploadDocumentID: RelativeLayout
    private lateinit var textDocumentId: TextView
    private lateinit var buttonProfileEdit: FloatingActionButton

    companion object {
        fun getInstance(): SettingsProfileDetailFragment {
            return SettingsProfileDetailFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        settingsProfileDetailPresenter.initialize(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings_profile_detail, container, false)
        profileImage = view.findViewById(R.id.profile_image)
        textProfileEmailAddress = view.findViewById(R.id.text_profile_email_address)
        textProfileFirstName = view.findViewById(R.id.text_profile_first_name)
        textProfileMiddleName = view.findViewById(R.id.text_profile_middle_name)
        textProfileLastName = view.findViewById(R.id.text_profile_last_name)
        textProfileAddress = view.findViewById(R.id.text_profile_address)
        textProfileGender = view.findViewById(R.id.text_profile_gender)
        textProfileDOB = view.findViewById(R.id.text_profile_dob)
        textProfileCellPhone = view.findViewById(R.id.text_profile_cell_phome_number)
        textDocumentId = view.findViewById(R.id.text_document_id)
        buttonUploadDocumentID = view.findViewById(R.id.buttonUploadDocumentID)
        buttonUploadDocumentID.setOnClickListener {
            val uploadDocumentIDFragment = SettingsProfileUploadDocumentIDFragment()
            fragmentManager.beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.settings_profile_activity_fragment_container, uploadDocumentIDFragment)
                    .commit()
        }
        buttonProfileEdit = view.findViewById(R.id.button_profile_edit)
        buttonProfileEdit.visibility = INVISIBLE
        buttonProfileEdit.setOnClickListener {
            val editFragment = SettingsProfileEditFragment.getInstance()
            fragmentManager.beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.settings_profile_activity_fragment_container, editFragment)
                    .commit()
        }
        if (activity is SettingsProfileActivity) {
            val a = activity as SettingsProfileActivity
            a.toolbar.title = getString(R.string.title_settings_profile)
            a.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            a.toolbar.setNavigationOnClickListener {
                a.finish()
            }
        }
        settingsProfileDetailPresenter.setupUserInfo()
        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        if (userModel.profileImageURL.isNotEmpty()) {
            Glide
                    .with(this.context)
                    .load(userModel.profileImageURL)
                    .into(profileImage)
        }

        val notSet = getString(R.string.not_set)
        textProfileEmailAddress.text = if (userModel.emailAddress.isBlank()) notSet else userModel.emailAddress
        textProfileFirstName.text = if (userModel.firstName.isBlank()) notSet else userModel.firstName
        textProfileMiddleName.text = if (userModel.middleName.isBlank()) notSet else userModel.middleName
        textProfileLastName.text = if (userModel.lastName.isBlank()) notSet else userModel.lastName
        textProfileAddress.text = if (userModel.address.isBlank()) notSet else userModel.address
        textProfileGender.text = if (userModel.genderType.getGender(context).isBlank()) notSet else userModel.genderType.getGender(context)
        textProfileDOB.text = if (userModel.dateOfBirth.isBlank()) notSet else userModel.dateOfBirth
        textProfileCellPhone.text = if (userModel.phoneNumber.getCellphoneNumberWithNationalCode().isBlank()) notSet else userModel.phoneNumber.getCellphoneNumberWithNationalCode()

        checkIdentificationStatus(userModel.identificationStatus)
    }

    private fun checkIdentificationStatus(status: IdentificationStatus) {
        val value: String
        buttonUploadDocumentID.isEnabled = true
        buttonProfileEdit.visibility = VISIBLE

        when (status) {
            IdentificationStatus.Applied -> {
                value = getString(R.string.under_review_for_id_document)
                buttonUploadDocumentID.isEnabled = false
            }
            IdentificationStatus.Identified -> {
                value = getString(R.string.id_document_was_approved)
                buttonUploadDocumentID.isEnabled = false
            }
            else -> {
                value = getString(R.string.upload_your_id_document)
            }
        }

        textDocumentId.text = value
    }

}
