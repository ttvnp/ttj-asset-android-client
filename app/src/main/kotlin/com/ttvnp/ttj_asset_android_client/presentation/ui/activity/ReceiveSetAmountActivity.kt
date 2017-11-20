package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.RadioGroup
import com.ttvnp.ttj_asset_android_client.domain.model.AssetType
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.ReceiveSetAmountPresenter
import dagger.android.AndroidInjection
import javax.inject.Inject

class ReceiveSetAmountActivity : BaseActivity() {

    companion object {
        val INTENT_EXTRA_KEY = "qr_string"
    }

    @Inject
    lateinit var receiveSetAmountPresenter : ReceiveSetAmountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receive_set_amount)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_receive_amount)
        toolbar.title = getString(R.string.title_set_amount)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent);
            finish()
        }

        val radioGroupReceive = findViewById<RadioGroup>(R.id.radio_group_receive)
        val textInputLayoutReceiveAmount = findViewById<TextInputLayout>(R.id.text_input_layout_receive_amount)
        val textReceiveAmount = findViewById<TextInputEditText>(R.id.text_receive_amount)

        // set on click
        val buttonTutorialSubmit = findViewById<Button>(R.id.button_tutorial_submit)
        buttonTutorialSubmit.setOnClickListener {
            val checkedId = radioGroupReceive.checkedRadioButtonId
            val selectedAssetType = if (checkedId == R.id.radio_receive_coin) AssetType.ASSET_TYPE_COIN else AssetType.ASSET_TYPE_POINT
            var amountString = textReceiveAmount.text.toString()
            if (amountString.isBlank()) {
                amountString = "0"
            }
            val amount: Long
            try {
                amount = amountString.toLong()
                receiveSetAmountPresenter.submitReceiveInfo(selectedAssetType, amount, { qrCodeInfoModel ->
                    val intent = Intent()
                    intent.putExtra(INTENT_EXTRA_KEY, qrCodeInfoModel.toQRString())
                    setResult(RESULT_OK, intent)
                    finish()
                }, { throwable ->
                    // do nothing
                })
            } catch (e: NumberFormatException) {
                textInputLayoutReceiveAmount.isErrorEnabled = true
                textInputLayoutReceiveAmount.error = getString(R.string.error_message_invalid_long)
            }
        }
    }
}
