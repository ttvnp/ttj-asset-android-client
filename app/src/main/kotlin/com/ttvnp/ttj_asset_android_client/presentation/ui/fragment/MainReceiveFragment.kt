package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.ttvnp.ttj_asset_android_client.domain.model.StellarAccountModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.ReceiveSetAmountActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainReceivePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainReceivePresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainReceiveFragment : BaseMainFragment(), MainReceivePresenterTarget {

    @Inject
    lateinit var mainReceivePresenter: MainReceivePresenter


    private lateinit var buttonSetAmount: Button
    private lateinit var imageQRCode: ImageView
    private lateinit var mReceiveOptionSpinner: Spinner
    private lateinit var mTextByStellarContainer: LinearLayout
    private lateinit var mTextStellarAccountId: TextView
    private lateinit var mTextStellarMemoText: TextView

    companion object {
        fun getInstance() : MainReceiveFragment {
            return MainReceiveFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mainReceivePresenter.init(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_receive, container, false)
        buttonSetAmount = view.findViewById(R.id.button_set_amount)
        imageQRCode = view.findViewById(R.id.image_qr_code)
        mReceiveOptionSpinner = view.findViewById(R.id.receive_option_spinner)
        mTextByStellarContainer = view.findViewById(R.id.text_by_stellar_container)
        mTextStellarAccountId = view.findViewById(R.id.text_str_account_id)
        mTextStellarMemoText = view.findViewById(R.id.text_memo)

        buttonSetAmount.setOnClickListener {
            val intent = Intent(activity, ReceiveSetAmountActivity::class.java)
            activity?.startActivityForResult(intent, RequestCode.SET_AMOUNT_ACTIVITY.rawValue)
        }
        mainReceivePresenter.setupDefault()
        val spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.receive_options, android.R.layout.simple_spinner_item)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mReceiveOptionSpinner.adapter = spinnerAdapter
        mReceiveOptionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                // do nothing
            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    mainReceivePresenter.setupDefault()
                    buttonSetAmount.visibility = View.VISIBLE
                    mTextByStellarContainer.visibility = View.GONE
                    return
                }
                mainReceivePresenter.getStellarAccount()
                buttonSetAmount.visibility = View.GONE
                mTextByStellarContainer.visibility = View.VISIBLE

            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        mainReceivePresenter.dispose()
    }

    @SuppressLint("SetTextI18n")
    override fun onGettingStellarAccount(stellarAccountModel: StellarAccountModel) {
        mTextStellarAccountId.text = getString(R.string.stellar_address) + "\t" + stellarAccountModel.strAccountID
        mTextStellarMemoText.text = getString(R.string.str_memo) + "\t" + stellarAccountModel.strDepositMemoText
    }

    override fun setQRCode(qrText: String) {
        val qrCode = Encoder.encode(qrText, ErrorCorrectionLevel.H)
        val byteMatrix = qrCode.matrix
        var bitmap = Bitmap.createBitmap(byteMatrix.width, byteMatrix.height, Bitmap.Config.ARGB_8888)
        for (y in 0 until byteMatrix.height) {
            for (x in 0 until byteMatrix.width) {
                val value = byteMatrix.get(x, y)
                bitmap.setPixel(x, y, if (value.toInt() == 1) Color.BLACK else Color.WHITE)
            }
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, imageQRCode.width, imageQRCode.height, false)
        imageQRCode.post {
            imageQRCode.setImageBitmap(bitmap)
        }
    }
}