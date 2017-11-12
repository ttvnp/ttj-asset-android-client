package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class MainHomeFragment : BaseMainFragment(), MainHomePresenterTarget {

    @Inject
    lateinit var mainHomePresenter: MainHomePresenter

    private lateinit var textEmailAddress: TextView
    private lateinit var profileImage: CircleImageView
    private lateinit var textPointAmount: TextView
    private lateinit var textCoinAmount: TextView

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
        profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        textPointAmount = view.findViewById<TextView>(R.id.text_point_amount)
        textCoinAmount = view.findViewById<TextView>(R.id.text_coint_amount)
        mainHomePresenter.setupUserInfo()
        mainHomePresenter.setupBalanceInfo()
        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        textEmailAddress.text = userModel.emailAddress
        if (0 < userModel.profileImageURL.length) {
            Picasso.with(this.context).load(userModel.profileImageURL).into(profileImage)
        }
    }

    override fun bindBalanceInfo(balancesModel: BalancesModel) {
        textPointAmount.text = balancesModel.pointBalance.getAmountFormatString()
        textCoinAmount.text = balancesModel.coinBalance.getAmountFormatString()
    }
}
