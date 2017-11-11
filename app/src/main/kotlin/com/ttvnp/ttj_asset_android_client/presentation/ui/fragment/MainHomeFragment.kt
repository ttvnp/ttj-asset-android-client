package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainHomeFragment : BaseMainFragment(), MainHomePresenterTarget {

    @Inject
    lateinit var mainHomePresenter: MainHomePresenter

    private lateinit var textEmailAddress: TextView

    companion object {
        fun getInstance() : MainHomeFragment {
            return MainHomeFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mainHomePresenter.init(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        textEmailAddress = view.findViewById<TextView>(R.id.text_email_address)
        mainHomePresenter.setupUserInfo()
        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        textEmailAddress.text = userModel.emailAddress
    }
}
