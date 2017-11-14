package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.util.isValidAmount
import com.ttvnp.ttj_asset_android_client.domain.util.prependIfNotBlank
import com.ttvnp.ttj_asset_android_client.domain.util.toAmount
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.SendInfoBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.SendInfoBridgeDataTranslator
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SendAmountFormPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class SendAmountFormFragment() : BaseMainFragment(), SendAmountFormPresenterTarget {

    @Inject
    lateinit var sendAmountFormPresenter: SendAmountFormPresenter

    private lateinit var imageSendTargetUserProfile: CircleImageView
    private lateinit var textSendTargetUser: TextView
    private lateinit var radioGroupSend: RadioGroup
    private lateinit var radioSendPoint: RadioButton
    private lateinit var radioSendCoin: RadioButton
    private lateinit var textInputLayoutSendAmount: TextInputLayout
    private lateinit var textSendAmount: TextView

    private var qrCodeString: String? = null
    private var sendInfoModel: SendInfoModel? = null
    var cancelButtonClickHandler: View.OnClickListener? = null

    companion object {
        val QR_STRING_ARG_KEY = "qr_string"
        fun getInstance() : SendAmountFormFragment {
            return SendAmountFormFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        qrCodeString = arguments.getString(QR_STRING_ARG_KEY)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(QR_STRING_ARG_KEY, qrCodeString)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_amount_form, container, false)
        imageSendTargetUserProfile = view.findViewById(R.id.image_send_target_user_profile)
        textSendTargetUser = view.findViewById(R.id.text_send_target_user)
        radioGroupSend = view.findViewById(R.id.radio_group_send)
        radioSendPoint = view.findViewById(R.id.radio_send_point)
        radioSendCoin = view.findViewById(R.id.radio_send_coin)
        textInputLayoutSendAmount = view.findViewById(R.id.text_input_layout_send_amount)
        textSendAmount = view.findViewById(R.id.text_send_amount)
        qrCodeString?.let {
            sendAmountFormPresenter.initialize(this, it)
        }
        val buttonSendAmountCancel = view.findViewById<Button>(R.id.button_send_amount_cancel)
        buttonSendAmountCancel.setOnClickListener(cancelButtonClickHandler)
        val buttonSendAmountSubmit = view.findViewById<Button>(R.id.button_send_amount_submit)
        buttonSendAmountSubmit.setOnClickListener {
            sendInfoModel?.let {
                val selectedAssetType
                        = if (radioGroupSend.checkedRadioButtonId == R.id.radio_send_coin) AssetType.ASSET_TYPE_COIN else AssetType.ASSET_TYPE_POINT
                val amountString = textSendAmount.text.toString()
                if (!amountString.isValidAmount()) {
                    textInputLayoutSendAmount.isErrorEnabled = true
                    textInputLayoutSendAmount.error = getString(R.string.error_message_invalid_long)
                    return@let
                }
                val amount = amountString.toAmount()
                val confirmFragment = SendAmountConfirmFragment.getInstance()
                confirmFragment.arguments = Bundle().apply {
                    val data = SendInfoBridgeData(
                            targetUserID = it.targetUserID,
                            targetUserEmailAddress = it.targetUserEmailAddress,
                            targetUserProfileImageID = it.targetUserProfileImageID,
                            targetUserProfileImageURL = it.targetUserProfileImageURL,
                            targetUserFirstName = it.targetUserFirstName,
                            targetUserMiddleName = it.targetUserMiddleName,
                            targetUserLastName = it.targetUserLastName,
                            assetType = selectedAssetType.rawValue,
                            amount = amount
                    )
                    this.putSerializable(SendAmountConfirmFragment.SEND_INFO_KEY, data)
                }
                fragmentManager.beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.send_activity_fragment_container, confirmFragment)
                        .commit()
            }
        }
        return view
    }

    override fun setSendInfo(sendInfoModel: SendInfoModel) {
        this.sendInfoModel = sendInfoModel
        if (0 < sendInfoModel.targetUserProfileImageURL.length) {
            Picasso.with(context).load(sendInfoModel.targetUserProfileImageURL).into(imageSendTargetUserProfile)
        }
        textSendTargetUser.text = buildTargetUserText(sendInfoModel)
        when (sendInfoModel.assetType) {
            AssetType.ASSET_TYPE_POINT -> {
                radioSendPoint.isChecked = true
            }
            AssetType.ASSET_TYPE_COIN -> {
                radioSendCoin.isChecked = true
            }
        }
        textSendAmount.text = sendInfoModel.amount.toString()
    }

    private fun buildTargetUserText(sendInfoModel: SendInfoModel): String {
        var userName = ""
        if (!sendInfoModel.targetUserFirstName.isBlank()) {
            userName += sendInfoModel.targetUserFirstName.prependIfNotBlank(" ")
        }
        if (!sendInfoModel.targetUserMiddleName.isBlank()) {
            userName += sendInfoModel.targetUserMiddleName.prependIfNotBlank(" ")
        }
        if (!sendInfoModel.targetUserLastName.isBlank()) {
            userName += sendInfoModel.targetUserLastName.prependIfNotBlank(" ")
        }
        return if (userName.isBlank()) sendInfoModel.targetUserEmailAddress else userName
    }
}
