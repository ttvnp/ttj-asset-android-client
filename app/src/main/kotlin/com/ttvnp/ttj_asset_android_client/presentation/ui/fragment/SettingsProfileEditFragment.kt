package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileDetailPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileEditPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileDetailPresenterTarget
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileEditPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class SettingsProfileEditFragment() : BaseMainFragment(), SettingsProfileEditPresenterTarget {

    @Inject
    lateinit var settingsProfileEditPresenter: SettingsProfileEditPresenter

    private lateinit var profileImage: CircleImageView
    private lateinit var buttonProfileImageEdit: FloatingActionButton
    private lateinit var textInputLayoutProfileEmailAddress: TextInputLayout
    private lateinit var textProfileEmailAddress: TextInputEditText
    private lateinit var textInputLayoutProfileFirstName: TextInputLayout
    private lateinit var textProfileFirstName: TextInputEditText
    private lateinit var textInputLayoutProfileMiddleName: TextInputLayout
    private lateinit var textProfileMiddleName: TextInputEditText
    private lateinit var textInputLayoutProfileLastName: TextInputLayout
    private lateinit var textProfileLastName: TextInputEditText
    private lateinit var textInputLayoutProfileAddress: TextInputLayout
    private lateinit var textProfileAddress: TextInputEditText

    companion object {
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
        textInputLayoutProfileEmailAddress = view.findViewById(R.id.text_input_layout_profile_email_address)
        textProfileEmailAddress = view.findViewById(R.id.text_profile_email_address)
        textInputLayoutProfileFirstName = view.findViewById(R.id.text_input_layout_profile_first_name)
        textProfileFirstName = view.findViewById(R.id.text_profile_first_name)
        textInputLayoutProfileMiddleName = view.findViewById(R.id.text_input_layout_profile_middle_name)
        textProfileMiddleName = view.findViewById(R.id.text_profile_middle_name)
        textInputLayoutProfileLastName = view.findViewById(R.id.text_input_layout_profile_last_name)
        textProfileLastName = view.findViewById(R.id.text_profile_last_name)
        textInputLayoutProfileAddress = view.findViewById(R.id.text_input_layout_profile_address)
        textProfileAddress = view.findViewById(R.id.text_profile_address)
        settingsProfileEditPresenter.setupUserInfo()
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
}