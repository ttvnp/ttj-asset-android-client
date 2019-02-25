package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.AlertDialog
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
        fun getInstance(): SendAmountFormByStellarFragment {
            return SendAmountFormByStellarFragment()
        }
    }

    @Inject
    lateinit var mPresenter: SendAmountFormByStellarPresenter

    private var mQrCodeByStellarInfo: QRCodeInfoStellarInfoModel? = null
    var cancelButtonClickHandler: View.OnClickListener? = null

    private lateinit var mTextLayoutStrAccountId: TextInputLayout
    private lateinit var mTextInputStrAccountId: EditText
    private lateinit var mTextLayoutInputMemo: TextInputLayout
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
        arguments?.let {
            val arg = it.getSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY)
            if (arg is QRCodeInfoStellarBridgeData) {
                mQrCodeByStellarInfo = QRCodeInfoStellarBirdgeDataTranslator().translate(arg)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mQrCodeByStellarInfo?.let {
            outState.putSerializable(
                    SendAmountFormFragment.QR_CODE_INFO_ARG_KEY,
                    QRCodeInfoStellarBirdgeDataTranslator().translate(it)
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
                R.layout.fragment_send_amount_form_by_stellar,
                container,
                false
        )
        mPresenter.initialize(this)
        initView(view)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dispose()
    }

    override fun navigateToConfirm(
            assetType: AssetType,
            amount: Long
    ) {
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
        fragmentManager?.beginTransaction()
                ?.addToBackStack("")
                ?.replace(R.id.send_activity_fragment_container, confirmFragment)
                ?.commit()
    }

    override fun showError(
            errorCode: ErrorCode,
            throwable: Throwable?
    ) {
        val msg = getString(errorMessageGenerator.convert(errorCode))
        when (errorCode) {
            ErrorCode.ERROR_VALIDATION_STELLAR_ACCOUNT -> mTextLayoutStrAccountId.error = msg
            ErrorCode.ERROR_VALIDATION_STELLAR_TRUST_LINE -> mTextLayoutStrAccountId.error = msg
            ErrorCode.ERROR_VALIDATION_AMOUNT_LONG -> showAmountValidationError(msg)
            ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT -> showAmountValidationError(msg)
            ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER -> showError(errorCode, throwable)
            else -> {
                showErrorDialog(msg, onClick = { _, whichButton ->
                    if (whichButton == DialogInterface.BUTTON_POSITIVE) {
                        activity?.finish()
                    }
                })
            }
        }
    }

    override fun onValidation(
            addressError: Int?,
            memoError: Int?,
            amountError: Int?
    ) {
        addressError?.let {
            mTextLayoutStrAccountId.error = context?.getString(addressError)
        }
        memoError?.let {
            mTextLayoutInputMemo.error = context?.getString(memoError)
        }
        amountError?.let {
            mTextInputLayoutSendAmount.error = context?.getString(amountError)
        }
    }

    private fun initView(view: View) {
        mTextLayoutStrAccountId = view.findViewById(R.id.text_input_layout_str_account_id)
        mTextInputStrAccountId = view.findViewById(R.id.text_input_str_account_id)
        mTextInputStrAccountId.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.stellar_address))
        mTextLayoutInputMemo = view.findViewById(R.id.text_input_layout_memo)
        mTextInputMemo = view.findViewById(R.id.text_input_memo)
        mTextInputMemo.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.memo))
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
            if (!mPresenter.isValid(
                            mTextInputStrAccountId.text.toString(),
                            mTextInputMemo.text.toString(),
                            mTextSendAmount.text.toString(),
                            getString(R.string.sencoin_admin_address),
                            getString(R.string.sencoinex_admin_address)
                    )
            ) return@setOnClickListener
            clearError()
            AlertDialog.Builder(context)
                    .setMessage(R.string.confirm_snc_message)
                    .setPositiveButton(R.string.send_button_text) { dialog, _ ->
                        val selectedAssetType =
                                if (mRadioGroupSend.checkedRadioButtonId == R.id.radio_send_coin)
                                    AssetType.ASSET_TYPE_COIN
                                else
                                    AssetType.ASSET_TYPE_POINT
                        val amountString = mTextSendAmount.text.toString()
                        mPresenter.checkValidationStellar(
                                mTextInputStrAccountId.text.toString(),
                                amountString,
                                selectedAssetType)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.default_cancel_button_text, null)
                    .create()
                    .show()

        }
    }

    private fun showAmountValidationError(msg: String) {
        mTextInputLayoutSendAmount.isErrorEnabled = true
        mTextInputLayoutSendAmount.error = msg
    }

    private fun clearError() {
        mTextLayoutStrAccountId.error = null
        mTextLayoutInputMemo.error = null
        mTextInputLayoutSendAmount.error = null
    }

}
