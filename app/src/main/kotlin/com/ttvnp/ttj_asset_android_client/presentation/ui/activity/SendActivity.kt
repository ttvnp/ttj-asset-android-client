package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import android.view.View
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeInfoStellarInfoModel
import com.ttvnp.ttj_asset_android_client.domain.model.QRCodeType
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.QRCodeInfoBridgeDataTranslator
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.QRCodeInfoStellarBirdgeDataTranslator
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormByStellarFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendEmailFormFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SendActivity : BaseActivity(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    companion object {
        const val INTENT_EXTRA_KEY = "qr_string"
        private const val INTENT_EXTRA_KEY_IS_STELLAR = "is_stellar"
        fun start(context: Context?, isStellar: Boolean = false) {
            val intent = Intent(context, SendActivity::class.java)
            intent.putExtra(INTENT_EXTRA_KEY_IS_STELLAR, isStellar)
            context?.startActivity(intent)
        }
    }

    private val isStellar by lazy { intent.getBooleanExtra(INTENT_EXTRA_KEY_IS_STELLAR, false) }

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
            if (!isStellar) {
                val formFragment = SendEmailFormFragment.getInstance()
                formFragment.cancelButtonClickHandler = View.OnClickListener { cancel() }
                this.addFragment(formFragment)
                return
            }
            val formByStellarFragment = SendAmountFormByStellarFragment.getInstance()
            formByStellarFragment.cancelButtonClickHandler = View.OnClickListener { cancel() }
            this.addFragment(formByStellarFragment)
        } else {
            val bundle = Bundle()
            val formFragment: Fragment
            val onClickListener = View.OnClickListener { cancel() }
            // load model from qrString
            val info = qrString.split(";")[0]
            when (info) {
                QRCodeType.BY_STELLAR_ACCOUNT.rawValue -> {
                    val data = QRCodeInfoStellarBirdgeDataTranslator().translate(QRCodeInfoStellarInfoModel.load(qrString))
                    formFragment = SendAmountFormByStellarFragment.getInstance()
                    bundle.putSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY, data)
                    formFragment.arguments = bundle
                    formFragment.cancelButtonClickHandler = onClickListener
                }
                QRCodeType.BY_EMAIL.rawValue -> {
                    val data = QRCodeInfoBridgeDataTranslator().translate(QRCodeInfoModel.load(qrString))
                    formFragment = SendAmountFormFragment.getInstance()
                    bundle.putSerializable(SendAmountFormFragment.QR_CODE_INFO_ARG_KEY, data)
                    formFragment.arguments = bundle
                    formFragment.cancelButtonClickHandler = onClickListener
                }
                else -> {
                    finish()
                    return
                }
            }
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.send_activity_fragment_container, formFragment)
                    .commit()
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.send_activity_fragment_container, fragment)
                .commit()
    }

}
