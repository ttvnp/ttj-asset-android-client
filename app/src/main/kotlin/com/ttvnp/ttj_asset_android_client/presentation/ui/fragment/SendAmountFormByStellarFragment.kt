package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoStellarInfoModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.QRCodeInfoStellarBirdgeDataTranslator
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.QRCodeInfoStellarBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.SendInfoBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.listener.getOnFocusChangeListener
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SendAmountFormByStellarPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendAmountFormByStellarPresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SendAmountFormByStellarFragment : BaseFragment(), SendAmountFormByStellarPresenterTarget {

    companion object {
        fun getInstance() : SendAmountFormByStellarFragment {
            return SendAmountFormByStellarFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SendAmountFormByStellarPresenter

    private var mQrCodeByStellarInfo: QRCodeInfoStellarInfoModel? = null
    var cancelButtonClickHandler: View.OnClickListener? = null

    private lateinit var mTextInputStrAccountId: EditText
    private lateinit var mTextInputMemo: EditText
    private lateinit var mRadioGroupSend: RadioGroup
    private lateinit var mRadioSendPoint: RadioButton
    private lateinit var mRadioSendCoin: RadioButton
    private lateinit var mTextInputLayoutSendAmount: TextInputLayout
    private lateinit var mTextSendAmount: TextView
    private lateinit var mBtnCancel: Button
    private lateinit var mBtnSubmit: Button

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (arguments == null) return
        val arg = arguments.getSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY)
        if (arg is QRCodeInfoStellarBridgeData) {
            mQrCodeByStellarInfo = QRCodeInfoStellarBirdgeDataTranslator().translate(arg)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        mQrCodeByStellarInfo?.let {
            outState?.putSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY, QRCodeInfoStellarBirdgeDataTranslator().translate(it))
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_amount_form_by_stellar, container, false)
        mPresenter.initialize(this)
        initView(view)
        return view
    }

    override fun navigateToConfirm(assetType: AssetType, amount: Long) {
        mTextInputStrAccountId.error = ""
        val bundle = Bundle()
        val confirmFragment = SendAmountConfirmFragment.getInstance()
        val data = SendInfoBridgeData(
                targetStrAccountID = mTextInputStrAccountId.text.toString(),
                targetStrMemoText = mTextInputMemo.text.toString(),
                assetType = assetType.rawValue,
                amount = amount
        )
        bundle.putSerializable(SendAmountConfirmFragment.SEND_INFO_KEY, data)
        confirmFragment.arguments = bundle
        fragmentManager.beginTransaction()
                .addToBackStack("")
                .replace(R.id.send_activity_fragment_container, confirmFragment)
                .commit()
    }

    override fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        val msg = errorMessageGenerator.generate(errorCode, throwable)
        when (errorCode) {
            ErrorCode.ERROR_VALIDATION_STELLAR_ACCOUNT -> mTextInputStrAccountId.error = msg
            ErrorCode.ERROR_VALIDATION_STELLAR_TRUST_LINE -> mTextInputStrAccountId.error = msg
            ErrorCode.ERROR_VALIDATION_AMOUNT_LONG -> showAmountValidationError(msg)
            ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT -> showAmountValidationError(msg)
            else -> {
                showErrorDialog(msg, onClick = { _, whichButton ->
                    if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                        this.activity.finish()
                    }
                })
            }
        }
    }

    override fun onValidation(addressError: Int?) {
        addressError?.let {  mTextInputStrAccountId.error = context.getString(addressError) }
    }

    private fun initView(view: View) {
        mTextInputStrAccountId = view.findViewById(R.id.text_input_str_account_id)
        mTextInputMemo = view.findViewById(R.id.text_input_memo)
        mRadioGroupSend = view.findViewById(R.id.radio_group_send)
        mRadioSendPoint = view.findViewById(R.id.radio_send_point)
        mRadioSendCoin = view.findViewById(R.id.radio_send_coin)
        mTextInputLayoutSendAmount = view.findViewById(R.id.text_input_layout_send_amount)
        mTextSendAmount = view.findViewById(R.id.text_send_amount)
        mTextSendAmount.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.hint_send_amount))
        mTextInputStrAccountId.setText(mQrCodeByStellarInfo?.strAccountId)
        mBtnCancel = view.findViewById(R.id.button_send_amount_cancel)
        mBtnSubmit = view.findViewById(R.id.button_send_amount_submit)

        mBtnCancel.setOnClickListener(cancelButtonClickHandler)
        mBtnSubmit.setOnClickListener {
            if (mPresenter.validateAddress(mTextInputStrAccountId.text.toString())) return@setOnClickListener
            val selectedAssetType
                    = if (mRadioGroupSend.checkedRadioButtonId == R.id.radio_send_coin) AssetType.ASSET_TYPE_COIN else AssetType.ASSET_TYPE_POINT
            val amountString = mTextSendAmount.text.toString()
            mPresenter.checkValidationStellar(
                    mTextInputStrAccountId.text.toString(),
                    amountString,
                    selectedAssetType)
        }
    }

    private fun showAmountValidationError(msg: String) {
        mTextInputLayoutSendAmount.isErrorEnabled = true
        mTextInputLayoutSendAmount.error = msg
    }

}
