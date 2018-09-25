package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoStellarInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeType
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.*
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormByStellarFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendEmailFormFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SendActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    companion object {
        val INTENT_EXTRA_KEY = "qr_string"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_send)
        toolbar.title = getString(R.string.title_send)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        val cancel: () -> Unit = {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }

        toolbar.setNavigationOnClickListener {
            cancel()
        }

        if (savedInstanceState != null) return
        val qrString: String? = intent.getStringExtra(INTENT_EXTRA_KEY)

        if (qrString == null || qrString.isBlank()) {
            val formFragment = SendEmailFormFragment.getInstance()
            formFragment.cancelButtonClickHandler = View.OnClickListener { cancel() }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.send_activity_fragment_container, formFragment)
                    .commit()
        } else {
            val bundle = Bundle()
            val formFragment: Fragment
            val onClickListener = View.OnClickListener { cancel() }
            // load model from qrString
            if (qrString.split(";")[0].toInt() == QRCodeType.BY_STELLAR_ACCOUNT.rawValue) {
                val data = QRCodeInfoStellarBirdgeDataTranslator().translate(QRCodeInfoStellarInfoModel.load(qrString))
                formFragment = SendAmountFormByStellarFragment.getInstance()
                bundle.putSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY, data)
                formFragment.arguments = bundle
                formFragment.cancelButtonClickHandler = onClickListener

            } else {
                val data = QRCodeInfoBridgeDataTranslator().translate(QRCodeInfoModel.load(qrString))
                formFragment = SendAmountFormFragment.getInstance()
                bundle.putSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY, data)
                formFragment.arguments = bundle
                formFragment.cancelButtonClickHandler = onClickListener
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.send_activity_fragment_container, formFragment)
                    .commit()
        }
    }
}
