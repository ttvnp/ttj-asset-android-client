package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.SendInfoModel
import com.ttvnp.ttj_asset_android_client.domain.util.prependIfNotBlank
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.SendInfoBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.SendInfoBridgeDataTranslator
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SendAmountConfirmPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountConfirmPresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject
import android.util.TypedValue
import android.view.*
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode


class SendAmountConfirmFragment() : BaseFragment(), SendAmountConfirmPresenterTarget {

    @Inject
    lateinit var sendAmountConfirmPresenter: SendAmountConfirmPresenter

    private lateinit var imageSendTargetUserProfile: CircleImageView
    private lateinit var textSendTargetUser: TextView
    private lateinit var textSendConfirmDesc: TextView
    private var popup: PopupWindow? = null

    private var sendInfoModel: SendInfoModel? = null

    companion object {
        val SEND_INFO_KEY = "send_info"
        fun getInstance() : SendAmountConfirmFragment {
            return SendAmountConfirmFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        val data = arguments.getSerializable(SEND_INFO_KEY) as SendInfoBridgeData
        sendInfoModel = SendInfoBridgeDataTranslator().translate(data)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putSerializable(SEND_INFO_KEY, SendInfoBridgeDataTranslator().translate(sendInfoModel!!))
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_amount_confirm, container, false)
        imageSendTargetUserProfile = view.findViewById(R.id.image_send_target_user_profile)
        textSendTargetUser = view.findViewById(R.id.text_send_target_user)
        textSendConfirmDesc = view.findViewById(R.id.text_send_confirm_desc)
        val buttonSendAmountCancel = view.findViewById<Button>(R.id.button_send_confirm_cancel)
        buttonSendAmountCancel.setOnClickListener {
            if (0 < fragmentManager.backStackEntryCount) {
                fragmentManager.popBackStack()
            }
        }
        val buttonSendAmountSubmit = view.findViewById<Button>(R.id.button_send_confirm_submit)
        buttonSendAmountSubmit.setOnClickListener {
            sendAmountConfirmPresenter.createTransaction(sendInfoModel!!)
        }
        sendAmountConfirmPresenter.initialize(this, this.sendInfoModel!!)
        return view
    }

    override fun setSendInfo(sendInfoModel: SendInfoModel) {
        this.sendInfoModel = sendInfoModel
        if (0 < sendInfoModel.targetUserProfileImageURL.length) {
            Picasso.with(context).load(sendInfoModel.targetUserProfileImageURL).into(imageSendTargetUserProfile)
        }
        val targetUserName = buildTargetUserText(sendInfoModel)
        textSendTargetUser.text = targetUserName
        textSendConfirmDesc.text = getString(R.string.send_confirm_desc_format).format(
                sendInfoModel.amount,
                sendInfoModel.assetType.rawValue,
                targetUserName
        )
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

    override fun onTransactionSuccess(sendInfoModel: SendInfoModel) {
        popup = PopupWindow(activity)
        popup?.apply {
            val popupView = View.inflate(this@SendAmountConfirmFragment.context, R.layout.view_popup, null)
            popupView.findViewById<Button>(R.id.button_popup_close).setOnClickListener {
                if (this.isShowing) {
                    this.dismiss()
                    activity.finish()
                }
            }
            popupView.findViewById<TextView>(R.id.text_popup_content).text = getString(R.string.send_successfully_executed)
            this.contentView = popupView
            this.isOutsideTouchable = true
            this.isFocusable = true
            val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics)
            this.setWidth(width.toInt())
            this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
            this.showAtLocation(textSendConfirmDesc, Gravity.CENTER, 0, 0);
        }
        firebaseAnalyticsHelper?.logAssetSendEvent(sendInfoModel.assetType, sendInfoModel.amount)
    }

    override fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        val msg: String
        when (errorCode) {
            ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER -> msg = resources.getString(R.string.error_cannot_connect_to_server)
            ErrorCode.ERROR_LOCKED_OUT -> msg = resources.getString(R.string.error_lockout_message)
            ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT -> msg = resources.getString(R.string.error_cannot_send_that_amount)
            else -> msg = resources.getString(R.string.error_default_message)
        }
        AlertDialog
                .Builder(this.context)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(msg)
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }

    override fun onDestroy() {
        popup?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        super.onDestroy()
    }
}