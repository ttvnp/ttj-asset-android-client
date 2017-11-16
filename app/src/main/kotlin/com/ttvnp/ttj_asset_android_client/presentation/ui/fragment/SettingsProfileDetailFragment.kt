package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsProfileDetailPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsProfileDetailPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class SettingsProfileDetailFragment() : BaseMainFragment(), SettingsProfileDetailPresenterTarget {

    @Inject
    lateinit var settingsProfileDetailPresenter: SettingsProfileDetailPresenter

    private lateinit var profileImage: CircleImageView
    private lateinit var textProfileEmailAddress: TextView
    private lateinit var textProfileFirstName: TextView
    private lateinit var textProfileMiddleName: TextView
    private lateinit var textProfileLastName: TextView
    private lateinit var textProfileAddress: TextView

    companion object {
        fun getInstance() : SettingsProfileDetailFragment {
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
    ) : View {
        val view = inflater.inflate(R.layout.fragment_settings_profile_detail, container, false)
        profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        textProfileEmailAddress = view.findViewById<TextView>(R.id.text_profile_email_address)
        textProfileFirstName = view.findViewById<TextView>(R.id.text_profile_first_name)
        textProfileMiddleName = view.findViewById<TextView>(R.id.text_profile_middle_name)
        textProfileLastName = view.findViewById<TextView>(R.id.text_profile_last_name)
        textProfileAddress = view.findViewById<TextView>(R.id.text_profile_address)
        settingsProfileDetailPresenter.setupUserInfo()
        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        if (0 < userModel.profileImageURL.length) {
            Picasso.with(this.context).load(userModel.profileImageURL).into(profileImage)
        }
        val notSet = getString(R.string.not_set)
        textProfileEmailAddress.text = if (userModel.emailAddress.isBlank()) notSet else userModel.emailAddress
        textProfileFirstName.text = if (userModel.firstName.isBlank()) notSet else userModel.firstName
        textProfileMiddleName.text = if (userModel.middleName.isBlank()) notSet else userModel.middleName
        textProfileLastName.text = if (userModel.lastName.isBlank()) notSet else userModel.lastName
        textProfileAddress.text = if (userModel.address.isBlank()) notSet else userModel.address
    }
}
